package cl.duoc.albalab.contenidos.exception;

import java.time.LocalDateTime;
import java.util.Map;

// Formato único de error de la API: la app muestra "message" y, si viene, el error de cada campo.
public record ApiErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> campos
) {
}
