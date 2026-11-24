package cl.duoc.albalab.vinculacion.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cl.duoc.albalab.vinculacion.Datos;
import cl.duoc.albalab.vinculacion.exception.GlobalExceptionHandler;
import cl.duoc.albalab.vinculacion.exception.ResourceNotFoundException;
import cl.duoc.albalab.vinculacion.model.EstadoSolicitud;
import cl.duoc.albalab.vinculacion.service.SolicitudService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class SolicitudControllerTest {
    private static final String VALIDA = """
            {"nombre":"Ana Pérez","telefono":"912345678","correo":"ana@mail.cl","direccion":"Calle 123",
             "personas":3,"fechaContacto":"15/12/2026","contactoPreferido":"CORREO","tipoAyuda":"DONACION",
             "aceptaContacto":true,"recibeNovedades":false}
            """;

    @Mock
    private SolicitudService service;
    private MockMvc mockMvc;

    @BeforeEach
    void preparar() {
        mockMvc = MockMvcBuilders.standaloneSetup(new SolicitudController(service))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void crearDevuelve201ConEstadoRecibida() throws Exception {
        when(service.crear(any())).thenReturn(Datos.solicitud(1L, EstadoSolicitud.RECIBIDA));

        mockMvc.perform(post("/solicitudes").contentType(MediaType.APPLICATION_JSON).content(VALIDA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("RECIBIDA"));
    }

    @Test
    void crearSinAceptarContactoDevuelve400ConElCampo() throws Exception {
        String invalida = VALIDA.replace("\"aceptaContacto\":true", "\"aceptaContacto\":false");

        mockMvc.perform(post("/solicitudes").contentType(MediaType.APPLICATION_JSON).content(invalida))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.aceptaContacto").exists());
    }

    @Test
    void listarDevuelveLasSolicitudes() throws Exception {
        when(service.listar()).thenReturn(List.of(Datos.solicitud(2L, EstadoSolicitud.EN_REVISION)));

        mockMvc.perform(get("/solicitudes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("EN_REVISION"));
    }

    @Test
    void obtenerInexistenteDevuelve404() throws Exception {
        when(service.obtenerPorId(9L)).thenThrow(new ResourceNotFoundException("La solicitud 9 no existe"));

        mockMvc.perform(get("/solicitudes/9"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("La solicitud 9 no existe"));
    }

    @Test
    void actualizarDevuelve200() throws Exception {
        when(service.actualizar(eq(1L), any())).thenReturn(Datos.solicitud(1L, EstadoSolicitud.RECIBIDA));

        mockMvc.perform(put("/solicitudes/1").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"telefono\":\"987654321\",\"contactoPreferido\":\"TELEFONO\",\"recibeNovedades\":true}"))
                .andExpect(status().isOk());
    }

    @Test
    void cambiarEstadoDevuelve200() throws Exception {
        when(service.cambiarEstado(eq(1L), any())).thenReturn(Datos.solicitud(1L, EstadoSolicitud.CONTACTADO));

        mockMvc.perform(patch("/solicitudes/1/estado").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"estado\":\"CONTACTADO\",\"observacion\":\"ok\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CONTACTADO"));
    }

    @Test
    void historialYExists() throws Exception {
        when(service.historial(1L)).thenReturn(List.of());
        when(service.existePorId(1L)).thenReturn(true);

        mockMvc.perform(get("/solicitudes/1/historial")).andExpect(status().isOk());
        mockMvc.perform(get("/solicitudes/1/exists")).andExpect(status().isOk());
    }

    @Test
    void eliminarDevuelve204() throws Exception {
        mockMvc.perform(delete("/solicitudes/1")).andExpect(status().isNoContent());

        verify(service).eliminar(1L);
    }
}
