package cl.duoc.albalab.contenidos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cl.duoc.albalab.contenidos.exception.GlobalExceptionHandler;
import cl.duoc.albalab.contenidos.model.Programa;
import cl.duoc.albalab.contenidos.repository.ProgramaRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class ProgramaControllerTest {
    @Mock
    private ProgramaRepository repo;
    private MockMvc mockMvc;
    private final Programa programa = new Programa(1L, "Escuela", "Resumen", "Detalle");

    @BeforeEach
    void preparar() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ProgramaController(repo))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void listarProgramas() throws Exception {
        when(repo.findAll()).thenReturn(List.of(programa));

        mockMvc.perform(get("/programas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Escuela"));
    }

    @Test
    void obtenerInexistenteDevuelve404() throws Exception {
        when(repo.findById(9L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/programas/9")).andExpect(status().isNotFound());
    }

    @Test
    void crearDevuelve201() throws Exception {
        when(repo.save(any(Programa.class))).thenAnswer(i -> {
            Programa p = i.getArgument(0);
            p.setId(4L);
            return p;
        });

        mockMvc.perform(post("/programas").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"Nuevo\",\"resumen\":\"R\",\"detalle\":\"D\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4));
    }

    @Test
    void crearSinTituloDevuelve400() throws Exception {
        mockMvc.perform(post("/programas").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"resumen\":\"R\",\"detalle\":\"D\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.titulo").exists());
    }

    @Test
    void actualizarDevuelve200() throws Exception {
        when(repo.findById(1L)).thenReturn(Optional.of(programa));
        when(repo.save(any(Programa.class))).thenAnswer(i -> i.getArgument(0));

        mockMvc.perform(put("/programas/1").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"Editado\",\"resumen\":\"R\",\"detalle\":\"D\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Editado"));
    }

    @Test
    void eliminarDevuelve204() throws Exception {
        when(repo.findById(1L)).thenReturn(Optional.of(programa));

        mockMvc.perform(delete("/programas/1")).andExpect(status().isNoContent());

        verify(repo).delete(programa);
    }
}
