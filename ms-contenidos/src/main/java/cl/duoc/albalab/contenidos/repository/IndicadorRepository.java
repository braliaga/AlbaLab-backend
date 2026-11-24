package cl.duoc.albalab.contenidos.repository;

import cl.duoc.albalab.contenidos.model.Indicador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndicadorRepository extends JpaRepository<Indicador, Long> {
}
