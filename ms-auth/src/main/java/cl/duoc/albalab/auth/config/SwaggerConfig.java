package cl.duoc.albalab.auth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI documentacion() {
        return new OpenAPI().info(new Info().title("AlbaLab · ms-auth").version("1.0.0")
                .description("Login de gestores de vinculación. Entrega el JWT que exige el api-gateway."));
    }
}
