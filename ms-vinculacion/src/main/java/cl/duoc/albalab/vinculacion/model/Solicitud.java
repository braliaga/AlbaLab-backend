package cl.duoc.albalab.vinculacion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "solicitudes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String telefono;
    private String correo;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private Integer personas;

    // Mismo formato que usa la app (dd/MM/aaaa).
    @Column(nullable = false)
    private String fechaContacto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContactoPreferido contactoPreferido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAyuda tipoAyuda;

    @Column(nullable = false)
    private boolean aceptaContacto;

    @Column(nullable = false)
    private boolean recibeNovedades;

    // Ruta de la foto en el teléfono que envió la solicitud (la imagen no viaja al servidor).
    private String fotoPath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolicitud estado;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaActualizacion;

    @PrePersist
    void alCrear() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = fechaCreacion;
        if (estado == null) {
            estado = EstadoSolicitud.RECIBIDA;
        }
    }

    @PreUpdate
    void alActualizar() {
        fechaActualizacion = LocalDateTime.now();
    }
}
