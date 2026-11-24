package cl.duoc.albalab.vinculacion.repository;

import cl.duoc.albalab.vinculacion.model.HistorialSolicitud;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface HistorialSolicitudRepository extends JpaRepository<HistorialSolicitud, Long> {
    List<HistorialSolicitud> findBySolicitudIdOrderByFechaCambioAsc(Long solicitudId);

    @Transactional
    void deleteBySolicitudId(Long solicitudId);
}
