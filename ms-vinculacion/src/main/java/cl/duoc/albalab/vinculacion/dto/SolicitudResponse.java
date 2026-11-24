package cl.duoc.albalab.vinculacion.dto;

import cl.duoc.albalab.vinculacion.model.ContactoPreferido;
import cl.duoc.albalab.vinculacion.model.EstadoSolicitud;
import cl.duoc.albalab.vinculacion.model.Solicitud;
import cl.duoc.albalab.vinculacion.model.TipoAyuda;
import java.time.LocalDateTime;

public record SolicitudResponse(
        Long id,
        String nombre,
        String telefono,
        String correo,
        String direccion,
        Integer personas,
        String fechaContacto,
        ContactoPreferido contactoPreferido,
        TipoAyuda tipoAyuda,
        boolean aceptaContacto,
        boolean recibeNovedades,
        String fotoPath,
        EstadoSolicitud estado,
        LocalDateTime fechaCreacion
) {
    public static SolicitudResponse fromModel(Solicitud s) {
        return new SolicitudResponse(s.getId(), s.getNombre(), s.getTelefono(), s.getCorreo(), s.getDireccion(),
                s.getPersonas(), s.getFechaContacto(), s.getContactoPreferido(), s.getTipoAyuda(),
                s.isAceptaContacto(), s.isRecibeNovedades(), s.getFotoPath(), s.getEstado(), s.getFechaCreacion());
    }
}
