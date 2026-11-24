package cl.duoc.albalab.vinculacion.dto;

import cl.duoc.albalab.vinculacion.model.ContactoPreferido;
import cl.duoc.albalab.vinculacion.model.Solicitud;
import cl.duoc.albalab.vinculacion.model.TipoAyuda;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// Datos que envía el formulario "Quiero colaborar". Las reglas son las mismas que valida la app.
public record SolicitudRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 3, message = "El nombre debe tener al menos 3 caracteres")
        String nombre,
        @Pattern(regexp = "^$|^\\d{9}$", message = "El teléfono debe tener 9 dígitos")
        String telefono,
        @Email(message = "El correo no tiene un formato válido")
        String correo,
        @NotBlank(message = "La dirección es obligatoria")
        String direccion,
        @NotNull(message = "La cantidad de personas es obligatoria")
        @Min(value = 1, message = "Debe haber al menos 1 persona")
        @Max(value = 500, message = "El máximo es 500 personas")
        Integer personas,
        @NotBlank(message = "La fecha de contacto es obligatoria")
        @Pattern(regexp = "^\\d{1,2}/\\d{1,2}/\\d{4}$", message = "La fecha debe tener el formato dd/MM/aaaa")
        String fechaContacto,
        @NotNull(message = "Elige un medio de contacto preferido")
        ContactoPreferido contactoPreferido,
        @NotNull(message = "Elige un tipo de ayuda")
        TipoAyuda tipoAyuda,
        // Boolean (no boolean): si el campo falta, la validación responde con un mensaje por campo en vez de un 400 genérico.
        @NotNull(message = "Debes aceptar que te contactemos")
        @AssertTrue(message = "Debes aceptar que te contactemos")
        Boolean aceptaContacto,
        Boolean recibeNovedades,
        String fotoPath
) {
    public Solicitud toModel() {
        Solicitud s = new Solicitud();
        s.setNombre(nombre.trim());
        s.setTelefono(telefono);
        s.setCorreo(correo);
        s.setDireccion(direccion.trim());
        s.setPersonas(personas);
        s.setFechaContacto(fechaContacto);
        s.setContactoPreferido(contactoPreferido);
        s.setTipoAyuda(tipoAyuda);
        s.setAceptaContacto(Boolean.TRUE.equals(aceptaContacto));
        s.setRecibeNovedades(Boolean.TRUE.equals(recibeNovedades));
        s.setFotoPath(fotoPath);
        return s;
    }
}
