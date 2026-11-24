package cl.duoc.albalab.auth.config;

import cl.duoc.albalab.auth.dto.RegistroRequest;
import cl.duoc.albalab.auth.repository.UsuarioRepository;
import cl.duoc.albalab.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Crea el gestor de prueba la primera vez, para poder entrar a "Mis registros" sin registrarse.
@Configuration
public class DatosIniciales {
    @Bean
    public CommandLineRunner crearGestorDePrueba(UsuarioRepository repo, AuthService authService,
                                                 @Value("${gestor.email}") String email,
                                                 @Value("${gestor.password}") String password) {
        return args -> {
            if (!repo.existsByEmailIgnoreCase(email)) {
                authService.registrar(new RegistroRequest(email, "Equipo de Vinculación", password));
            }
        };
    }
}
