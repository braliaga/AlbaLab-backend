package cl.duoc.albalab.contenidos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI documentacion() {
        return new OpenAPI().info(new Info().title("AlbaLab · ms-contenidos").version("1.0.0")
                .description("Contenido institucional publicado en la app: programas, casos de éxito, catálogo, "
                        + "indicadores de impacto y destino de los aportes."));
    }
}
