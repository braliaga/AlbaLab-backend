package cl.duoc.albalab.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.duoc.albalab.auth.dto.LoginRequest;
import cl.duoc.albalab.auth.dto.RegistroRequest;
import cl.duoc.albalab.auth.dto.TokenResponse;
import cl.duoc.albalab.auth.exception.BadRequestException;
import cl.duoc.albalab.auth.model.Usuario;
import cl.duoc.albalab.auth.repository.UsuarioRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UsuarioRepository repo;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    private final JwtService jwt = new JwtService("clave-de-pruebas-de-al-menos-32-bytes!!", 3600);
    private AuthService service;
    private Usuario gestor;

    @BeforeEach
    void preparar() {
        service = new AuthService(repo, encoder, jwt);
        gestor = new Usuario(1L, "gestor@albalab.cl", "Gestor", encoder.encode("Alba2026!"), AuthService.ROL_GESTOR);
    }

    @Test
    void loginCorrectoEntregaTokenConElCorreo() {
        when(repo.findByEmailIgnoreCase("gestor@albalab.cl")).thenReturn(Optional.of(gestor));

        TokenResponse r = service.login(new LoginRequest("gestor@albalab.cl", "Alba2026!"));

        assertEquals("gestor@albalab.cl", jwt.emailDe(r.token()));
        assertEquals(3600, r.expiraEnSegundos());
    }

    @Test
    void loginConClaveErroneaLanzaCredencialesInvalidas() {
        when(repo.findByEmailIgnoreCase("gestor@albalab.cl")).thenReturn(Optional.of(gestor));

        assertThrows(CredencialesInvalidasException.class,
                () -> service.login(new LoginRequest("gestor@albalab.cl", "otra")));
    }

    @Test
    void loginConCorreoInexistenteLanzaCredencialesInvalidas() {
        when(repo.findByEmailIgnoreCase("nadie@albalab.cl")).thenReturn(Optional.empty());

        assertThrows(CredencialesInvalidasException.class,
                () -> service.login(new LoginRequest("nadie@albalab.cl", "x")));
    }

    @Test
    void registrarGuardaHashBCryptYNoLaClave() {
        when(repo.existsByEmailIgnoreCase("nuevo@albalab.cl")).thenReturn(false);
        when(repo.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        Usuario u = service.registrar(new RegistroRequest("Nuevo@AlbaLab.cl ", "Nuevo", "claveSegura1"));

        assertEquals("nuevo@albalab.cl", u.getEmail());
        assertNotEquals("claveSegura1", u.getPasswordHash());
        assertEquals(true, encoder.matches("claveSegura1", u.getPasswordHash()));
    }

    @Test
    void registrarCorreoRepetidoLanzaBadRequest() {
        when(repo.existsByEmailIgnoreCase("gestor@albalab.cl")).thenReturn(true);

        assertThrows(BadRequestException.class,
                () -> service.registrar(new RegistroRequest("gestor@albalab.cl", "X", "claveSegura1")));
        verify(repo, never()).save(any());
    }
}
