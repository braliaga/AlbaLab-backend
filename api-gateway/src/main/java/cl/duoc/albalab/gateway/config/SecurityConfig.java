package cl.duoc.albalab.gateway.config;

import java.nio.charset.StandardCharsets;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String secret;

    // Públicas: login, lectura de contenidos y el envío del formulario de interés (lo hace un visitante sin cuenta).
    // Todo lo demás (listar, editar, cambiar estado y eliminar solicitudes) exige el token del gestor.
    @Bean
    public SecurityWebFilterChain filtroSeguridad(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(reglas -> reglas
                        .pathMatchers("/auth/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/programas/**", "/casos/**", "/productos/**",
                                "/indicadores/**", "/destinos/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/solicitudes").permitAll()
                        .anyExchange().authenticated())
                .oauth2ResourceServer(o -> o.jwt(Customizer.withDefaults()))
                .build();
    }

    // HS256 fijo: debe coincidir con el algoritmo con que ms-auth firma el token.
    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        SecretKeySpec clave = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return NimbusReactiveJwtDecoder.withSecretKey(clave).macAlgorithm(MacAlgorithm.HS256).build();
    }
}
