package cl.duoc.albalab.contenidos.service;

import cl.duoc.albalab.contenidos.exception.ResourceNotFoundException;
import cl.duoc.albalab.contenidos.model.Identificable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

// CRUD común a los cinco tipos de contenido; cada controlador lo crea con su repositorio.
public class ContenidoService<T extends Identificable> {
    private final JpaRepository<T, Long> repository;
    private final String nombre;

    public ContenidoService(JpaRepository<T, Long> repository, String nombre) {
        this.repository = repository;
        this.nombre = nombre;
    }

    public List<T> listar() {
        return repository.findAll();
    }

    public T obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(nombre + " " + id + " no existe"));
    }

    public T crear(T nuevo) {
        nuevo.setId(null);
        return repository.save(nuevo);
    }

    public T actualizar(Long id, T datos) {
        obtenerPorId(id);
        datos.setId(id);
        return repository.save(datos);
    }

    public void eliminar(Long id) {
        repository.delete(obtenerPorId(id));
    }
}
