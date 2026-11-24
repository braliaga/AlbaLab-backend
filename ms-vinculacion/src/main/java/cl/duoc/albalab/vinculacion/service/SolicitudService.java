package cl.duoc.albalab.vinculacion.service;

import cl.duoc.albalab.vinculacion.dto.CambioEstadoRequest;
import cl.duoc.albalab.vinculacion.dto.SolicitudRequest;
import cl.duoc.albalab.vinculacion.dto.SolicitudUpdateRequest;
import cl.duoc.albalab.vinculacion.exception.BadRequestException;
import cl.duoc.albalab.vinculacion.exception.ResourceNotFoundException;
import cl.duoc.albalab.vinculacion.model.EstadoSolicitud;
import cl.duoc.albalab.vinculacion.model.HistorialSolicitud;
import cl.duoc.albalab.vinculacion.model.Solicitud;
import cl.duoc.albalab.vinculacion.repository.HistorialSolicitudRepository;
import cl.duoc.albalab.vinculacion.repository.SolicitudRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SolicitudService {
    private final SolicitudRepository solicitudRepository;
    private final HistorialSolicitudRepository historialRepository;

    public SolicitudService(SolicitudRepository solicitudRepository, HistorialSolicitudRepository historialRepository) {
        this.solicitudRepository = solicitudRepository;
        this.historialRepository = historialRepository;
    }

    public List<Solicitud> listar() {
        return solicitudRepository.findAllByOrderByIdDesc();
    }

    public Solicitud obtenerPorId(Long id) {
        return solicitudRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La solicitud " + id + " no existe"));
    }

    public boolean existePorId(Long id) {
        return solicitudRepository.existsById(id);
    }

    // Regla de negocio: se exige al menos un medio de contacto (correo o teléfono).
    @Transactional
    public Solicitud crear(SolicitudRequest req) {
        if (vacio(req.telefono()) && vacio(req.correo())) {
            throw new BadRequestException("Indica al menos un medio de contacto: teléfono o correo");
        }
        Solicitud nueva = req.toModel();
        nueva.setEstado(EstadoSolicitud.RECIBIDA);
        Solicitud guardada = solicitudRepository.save(nueva);
        registrarHistorial(guardada.getId(), null, EstadoSolicitud.RECIBIDA, "Solicitud recibida desde la app");
        return guardada;
    }

    @Transactional
    public Solicitud actualizar(Long id, SolicitudUpdateRequest req) {
        Solicitud s = obtenerPorId(id);
        s.setTelefono(req.telefono());
        s.setContactoPreferido(req.contactoPreferido());
        s.setRecibeNovedades(Boolean.TRUE.equals(req.recibeNovedades()));
        return solicitudRepository.save(s);
    }

    @Transactional
    public Solicitud cambiarEstado(Long id, CambioEstadoRequest req) {
        Solicitud s = obtenerPorId(id);
        EstadoSolicitud anterior = s.getEstado();
        if (anterior == req.estado()) {
            throw new BadRequestException("La solicitud ya está en estado " + anterior);
        }
        if (anterior.esFinal()) {
            throw new BadRequestException("Una solicitud " + anterior + " no puede cambiar de estado");
        }
        s.setEstado(req.estado());
        Solicitud guardada = solicitudRepository.save(s);
        registrarHistorial(id, anterior, req.estado(), req.observacion());
        return guardada;
    }

    public List<HistorialSolicitud> historial(Long id) {
        obtenerPorId(id);
        return historialRepository.findBySolicitudIdOrderByFechaCambioAsc(id);
    }

    @Transactional
    public void eliminar(Long id) {
        Solicitud s = obtenerPorId(id);
        historialRepository.deleteBySolicitudId(id);
        solicitudRepository.delete(s);
    }

    private void registrarHistorial(Long solicitudId, EstadoSolicitud anterior, EstadoSolicitud nuevo, String obs) {
        historialRepository.save(new HistorialSolicitud(null, solicitudId, anterior, nuevo, obs, LocalDateTime.now()));
    }

    private static boolean vacio(String valor) {
        return valor == null || valor.isBlank();
    }
}
