package cl.duoc.albalab.contenidos.controller;

import cl.duoc.albalab.contenidos.model.Identificable;
import cl.duoc.albalab.contenidos.service.ContenidoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

// Endpoints CRUD heredados por cada recurso (/programas, /casos, /productos, /indicadores, /destinos).
public abstract class ContenidoController<T extends Identificable> {
    protected final ContenidoService<T> service;

    protected ContenidoController(ContenidoService<T> service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<T>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<T> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<T> crear(@Valid @RequestBody T nuevo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(nuevo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<T> actualizar(@PathVariable Long id, @Valid @RequestBody T datos) {
        return ResponseEntity.ok(service.actualizar(id, datos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
