package cl.duoc.albalab.contenidos.repository;

import cl.duoc.albalab.contenidos.model.Programa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramaRepository extends JpaRepository<Programa, Long> {
}
