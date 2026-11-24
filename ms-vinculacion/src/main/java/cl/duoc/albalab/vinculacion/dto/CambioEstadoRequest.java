package cl.duoc.albalab.vinculacion.dto;

import cl.duoc.albalab.vinculacion.model.EstadoSolicitud;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CambioEstadoRequest(
        @NotNull(message = "El estado es obligatorio")
        EstadoSolicitud estado,
        @Size(max = 500, message = "La observación admite hasta 500 caracteres")
        String observacion
) {
}
