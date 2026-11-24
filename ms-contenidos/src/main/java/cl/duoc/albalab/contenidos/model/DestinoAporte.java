package cl.duoc.albalab.contenidos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "destinos_aporte")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DestinoAporte implements Identificable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El área es obligatoria")
    private String area;

    @NotBlank(message = "El porcentaje es obligatorio")
    private String porcentaje;

    @NotBlank(message = "La explicación es obligatoria")
    @Column(length = 1000)
    private String explicacion;
}
