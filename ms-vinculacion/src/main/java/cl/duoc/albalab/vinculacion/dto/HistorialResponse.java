package cl.duoc.albalab.vinculacion.dto;

import cl.duoc.albalab.vinculacion.model.EstadoSolicitud;
import cl.duoc.albalab.vinculacion.model.HistorialSolicitud;
import java.time.LocalDateTime;

public record HistorialResponse(Long id, EstadoSolicitud estadoAnterior, EstadoSolicitud estadoNuevo,
                                String observacion, LocalDateTime fechaCambio) {
    public static HistorialResponse fromModel(HistorialSolicitud h) {
        return new HistorialResponse(h.getId(), h.getEstadoAnterior(), h.getEstadoNuevo(), h.getObservacion(),
                h.getFechaCambio());
    }
}
