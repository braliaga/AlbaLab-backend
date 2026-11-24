package cl.duoc.albalab.vinculacion.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.duoc.albalab.vinculacion.Datos;
import cl.duoc.albalab.vinculacion.dto.CambioEstadoRequest;
import cl.duoc.albalab.vinculacion.dto.SolicitudUpdateRequest;
import cl.duoc.albalab.vinculacion.exception.BadRequestException;
import cl.duoc.albalab.vinculacion.exception.ResourceNotFoundException;
import cl.duoc.albalab.vinculacion.model.ContactoPreferido;
import cl.duoc.albalab.vinculacion.model.EstadoSolicitud;
import cl.duoc.albalab.vinculacion.model.HistorialSolicitud;
import cl.duoc.albalab.vinculacion.model.Solicitud;
import cl.duoc.albalab.vinculacion.repository.HistorialSolicitudRepository;
import cl.duoc.albalab.vinculacion.repository.SolicitudRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SolicitudServiceTest {
    @Mock
    private SolicitudRepository repo;
    @Mock
    private HistorialSolicitudRepository historialRepo;
    private SolicitudService service;

    @BeforeEach
    void preparar() {
        service = new SolicitudService(repo, historialRepo);
    }

    @Test
    void crearQuedaRecibidaYRegistraHistorial() {
        when(repo.save(any(Solicitud.class))).thenAnswer(i -> {
            Solicitud s = i.getArgument(0);
            s.setId(7L);
            return s;
        });

        Solicitud creada = service.crear(Datos.request("912345678", ""));

        assertEquals(EstadoSolicitud.RECIBIDA, creada.getEstado());
        ArgumentCaptor<HistorialSolicitud> h = ArgumentCaptor.forClass(HistorialSolicitud.class);
        verify(historialRepo).save(h.capture());
        assertEquals(7L, h.getValue().getSolicitudId());
        assertNull(h.getValue().getEstadoAnterior());
    }

    @Test
    void crearSinTelefonoNiCorreoLanzaBadRequest() {
        assertThrows(BadRequestException.class, () -> service.crear(Datos.request("", " ")));
        verify(repo, never()).save(any());
    }

    @Test
    void obtenerInexistenteLanzaNotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.obtenerPorId(99L));
    }

    @Test
    void listarDevuelveLoDelRepositorio() {
        when(repo.findAllByOrderByIdDesc()).thenReturn(List.of(Datos.solicitud(2L, EstadoSolicitud.RECIBIDA)));

        assertEquals(1, service.listar().size());
    }

    @Test
    void existePorIdConsultaElRepositorio() {
        when(repo.existsById(1L)).thenReturn(true);

        assertTrue(service.existePorId(1L));
    }

    @Test
    void actualizarCambiaSoloLosCamposEditables() {
        Solicitud s = Datos.solicitud(1L, EstadoSolicitud.RECIBIDA);
        when(repo.findById(1L)).thenReturn(Optional.of(s));
        when(repo.save(s)).thenReturn(s);

        Solicitud r = service.actualizar(1L, new SolicitudUpdateRequest("987654321", ContactoPreferido.TELEFONO, true));

        assertEquals("987654321", r.getTelefono());
        assertEquals(ContactoPreferido.TELEFONO, r.getContactoPreferido());
        assertTrue(r.isRecibeNovedades());
        assertEquals("Ana Pérez", r.getNombre());
    }

    @Test
    void cambiarEstadoGuardaHistorialConElEstadoAnterior() {
        Solicitud s = Datos.solicitud(1L, EstadoSolicitud.RECIBIDA);
        when(repo.findById(1L)).thenReturn(Optional.of(s));
        when(repo.save(s)).thenReturn(s);

        service.cambiarEstado(1L, new CambioEstadoRequest(EstadoSolicitud.CONTACTADO, "Llamada realizada"));

        ArgumentCaptor<HistorialSolicitud> h = ArgumentCaptor.forClass(HistorialSolicitud.class);
        verify(historialRepo).save(h.capture());
        assertEquals(EstadoSolicitud.RECIBIDA, h.getValue().getEstadoAnterior());
        assertEquals(EstadoSolicitud.CONTACTADO, h.getValue().getEstadoNuevo());
        assertEquals(EstadoSolicitud.CONTACTADO, s.getEstado());
    }

    @Test
    void cambiarAlMismoEstadoLanzaBadRequest() {
        when(repo.findById(1L)).thenReturn(Optional.of(Datos.solicitud(1L, EstadoSolicitud.EN_REVISION)));

        assertThrows(BadRequestException.class,
                () -> service.cambiarEstado(1L, new CambioEstadoRequest(EstadoSolicitud.EN_REVISION, null)));
    }

    @Test
    void unEstadoFinalNoPuedeCambiar() {
        when(repo.findById(1L)).thenReturn(Optional.of(Datos.solicitud(1L, EstadoSolicitud.CONCRETADO)));

        assertThrows(BadRequestException.class,
                () -> service.cambiarEstado(1L, new CambioEstadoRequest(EstadoSolicitud.EN_REVISION, null)));
        verify(historialRepo, never()).save(any());
    }

    @Test
    void historialDeSolicitudExistente() {
        when(repo.findById(1L)).thenReturn(Optional.of(Datos.solicitud(1L, EstadoSolicitud.RECIBIDA)));
        when(historialRepo.findBySolicitudIdOrderByFechaCambioAsc(1L)).thenReturn(List.of());

        assertTrue(service.historial(1L).isEmpty());
    }

    @Test
    void eliminarBorraHistorialYSolicitud() {
        Solicitud s = Datos.solicitud(1L, EstadoSolicitud.RECIBIDA);
        when(repo.findById(1L)).thenReturn(Optional.of(s));

        service.eliminar(1L);

        verify(historialRepo).deleteBySolicitudId(1L);
        verify(repo).delete(s);
    }

    @Test
    void estadosFinales() {
        assertTrue(EstadoSolicitud.DESCARTADO.esFinal());
        assertFalse(EstadoSolicitud.CONTACTADO.esFinal());
    }
}
