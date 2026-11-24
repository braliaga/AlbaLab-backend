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
@Table(name = "casos_exito")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CasoExito implements Identificable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El alias es obligatorio (historias anonimizadas)")
    private String alias;

    @NotBlank(message = "La historia es obligatoria")
    @Column(length = 1000)
    private String historia;

    @NotBlank(message = "El logro es obligatorio")
    private String logro;
}
