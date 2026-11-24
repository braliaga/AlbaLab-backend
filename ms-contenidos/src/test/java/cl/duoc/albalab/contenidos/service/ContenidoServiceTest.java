package cl.duoc.albalab.contenidos.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.duoc.albalab.contenidos.exception.ResourceNotFoundException;
import cl.duoc.albalab.contenidos.model.Programa;
import cl.duoc.albalab.contenidos.repository.ProgramaRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContenidoServiceTest {
    @Mock
    private ProgramaRepository repo;
    private ContenidoService<Programa> service;
    private final Programa programa = new Programa(1L, "Escuela", "Resumen", "Detalle");

    @BeforeEach
    void preparar() {
        service = new ContenidoService<>(repo, "El programa");
    }

    @Test
    void listarDevuelveTodos() {
        when(repo.findAll()).thenReturn(List.of(programa));

        assertEquals(1, service.listar().size());
    }

    @Test
    void obtenerInexistenteLanzaNotFoundConMensaje() {
        when(repo.findById(9L)).thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> service.obtenerPorId(9L));
        assertEquals("El programa 9 no existe", e.getMessage());
    }

    @Test
    void crearIgnoraElIdQueVengaEnElCuerpo() {
        when(repo.save(any(Programa.class))).thenAnswer(i -> i.getArgument(0));

        Programa creado = service.crear(new Programa(55L, "Nuevo", "R", "D"));

        assertNull(creado.getId());
    }

    @Test
    void actualizarUsaElIdDeLaRuta() {
        when(repo.findById(1L)).thenReturn(Optional.of(programa));
        when(repo.save(any(Programa.class))).thenAnswer(i -> i.getArgument(0));

        Programa r = service.actualizar(1L, new Programa(null, "Editado", "R", "D"));

        assertEquals(1L, r.getId());
        assertEquals("Editado", r.getTitulo());
    }

    @Test
    void actualizarInexistenteNoGuarda() {
        when(repo.findById(9L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.actualizar(9L, programa));
        verify(repo, never()).save(any());
    }

    @Test
    void eliminarBorraElExistente() {
        when(repo.findById(1L)).thenReturn(Optional.of(programa));

        service.eliminar(1L);

        verify(repo).delete(programa);
    }
}
