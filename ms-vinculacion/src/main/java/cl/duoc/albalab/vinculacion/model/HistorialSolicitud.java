package cl.duoc.albalab.vinculacion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Auditoría de cada cambio de estado, como pide la propuesta técnica (tabla historial_solicitud).
@Entity
@Table(name = "historial_solicitud")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialSolicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long solicitudId;

    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estadoAnterior;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolicitud estadoNuevo;

    @Column(length = 500)
    private String observacion;

    @Column(nullable = false)
    private LocalDateTime fechaCambio;
}
