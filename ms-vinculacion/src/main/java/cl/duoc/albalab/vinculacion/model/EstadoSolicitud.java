package cl.duoc.albalab.vinculacion.model;

public enum EstadoSolicitud {
    RECIBIDA, EN_REVISION, CONTACTADO, CONCRETADO, DESCARTADO;

    // Una solicitud concretada o descartada ya no cambia de estado.
    public boolean esFinal() {
        return this == CONCRETADO || this == DESCARTADO;
    }
}
