package cl.duoc.albalab.vinculacion.repository;

import cl.duoc.albalab.vinculacion.model.Solicitud;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    List<Solicitud> findAllByOrderByIdDesc();
}
