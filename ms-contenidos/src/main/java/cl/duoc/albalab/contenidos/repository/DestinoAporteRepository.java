package cl.duoc.albalab.contenidos.repository;

import cl.duoc.albalab.contenidos.model.DestinoAporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinoAporteRepository extends JpaRepository<DestinoAporte, Long> {
}
