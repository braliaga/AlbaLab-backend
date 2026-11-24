package cl.duoc.albalab.vinculacion.controller;

import cl.duoc.albalab.vinculacion.dto.CambioEstadoRequest;
import cl.duoc.albalab.vinculacion.dto.HistorialResponse;
import cl.duoc.albalab.vinculacion.dto.SolicitudRequest;
import cl.duoc.albalab.vinculacion.dto.SolicitudResponse;
import cl.duoc.albalab.vinculacion.dto.SolicitudUpdateRequest;
import cl.duoc.albalab.vinculacion.service.SolicitudService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/solicitudes")
public class SolicitudController {
    private final SolicitudService service;

    public SolicitudController(SolicitudService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<SolicitudResponse>> listar() {
        return ResponseEntity.ok(service.listar().stream().map(SolicitudResponse::fromModel).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(SolicitudResponse.fromModel(service.obtenerPorId(id)));
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existe(@PathVariable Long id) {
        return ResponseEntity.ok(service.existePorId(id));
    }

    @PostMapping
    public ResponseEntity<SolicitudResponse> crear(@Valid @RequestBody SolicitudRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(SolicitudResponse.fromModel(service.crear(req)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitudResponse> actualizar(@PathVariable Long id,
                                                        @Valid @RequestBody SolicitudUpdateRequest req) {
        return ResponseEntity.ok(SolicitudResponse.fromModel(service.actualizar(id, req)));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<SolicitudResponse> cambiarEstado(@PathVariable Long id,
                                                           @Valid @RequestBody CambioEstadoRequest req) {
        return ResponseEntity.ok(SolicitudResponse.fromModel(service.cambiarEstado(id, req)));
    }

    @GetMapping("/{id}/historial")
    public ResponseEntity<List<HistorialResponse>> historial(@PathVariable Long id) {
        return ResponseEntity.ok(service.historial(id).stream().map(HistorialResponse::fromModel).toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
