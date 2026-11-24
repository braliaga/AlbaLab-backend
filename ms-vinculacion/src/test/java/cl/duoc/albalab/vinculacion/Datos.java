package cl.duoc.albalab.vinculacion;

import cl.duoc.albalab.vinculacion.dto.SolicitudRequest;
import cl.duoc.albalab.vinculacion.model.ContactoPreferido;
import cl.duoc.albalab.vinculacion.model.EstadoSolicitud;
import cl.duoc.albalab.vinculacion.model.Solicitud;
import cl.duoc.albalab.vinculacion.model.TipoAyuda;
import java.time.LocalDateTime;

public final class Datos {
    private Datos() {
    }

    public static SolicitudRequest request(String telefono, String correo) {
        return new SolicitudRequest("Ana Pérez", telefono, correo, "Calle 123", 3, "15/12/2026",
                ContactoPreferido.CORREO, TipoAyuda.DONACION, true, false, null);
    }

    public static Solicitud solicitud(Long id, EstadoSolicitud estado) {
        Solicitud s = request("912345678", "ana@mail.cl").toModel();
        s.setId(id);
        s.setEstado(estado);
        s.setFechaCreacion(LocalDateTime.of(2026, 11, 23, 10, 0));
        return s;
    }
}
