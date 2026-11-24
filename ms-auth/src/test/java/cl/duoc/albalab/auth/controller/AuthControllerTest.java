package cl.duoc.albalab.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cl.duoc.albalab.auth.dto.TokenResponse;
import cl.duoc.albalab.auth.exception.GlobalExceptionHandler;
import cl.duoc.albalab.auth.service.AuthService;
import cl.duoc.albalab.auth.service.CredencialesInvalidasException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Mock
    private AuthService authService;
    private MockMvc mockMvc;

    @BeforeEach
    void preparar() {
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(authService))
                .setControllerAdvice(new GlobalExceptionHandler(), new AuthErrorHandler())
                .build();
    }

    @Test
    void loginDevuelveToken() throws Exception {
        when(authService.login(any())).thenReturn(new TokenResponse("abc", "gestor@albalab.cl", "Gestor", 3600));

        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"gestor@albalab.cl\",\"password\":\"Alba2026!\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("abc"));
    }

    @Test
    void loginConCredencialesMalasDevuelve401() throws Exception {
        when(authService.login(any())).thenThrow(new CredencialesInvalidasException());

        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"gestor@albalab.cl\",\"password\":\"mala\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Correo o contraseña incorrectos"));
    }

    @Test
    void loginConCorreoInvalidoDevuelve400ConElCampo() throws Exception {
        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"no-es-correo\",\"password\":\"x\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.email").exists());
    }
}
