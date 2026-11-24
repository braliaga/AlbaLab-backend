package cl.duoc.albalab.vinculacion.dto;

import cl.duoc.albalab.vinculacion.model.ContactoPreferido;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

// Lo que el gestor puede corregir desde "Editar registro" (formulario 2 de la app).
public record SolicitudUpdateRequest(
        @NotNull(message = "El teléfono es obligatorio")
        @Pattern(regexp = "^\\d{9}$", message = "El teléfono debe tener 9 dígitos")
        String telefono,
        @NotNull(message = "Elige un medio de contacto preferido")
        ContactoPreferido contactoPreferido,
        Boolean recibeNovedades
) {
}
