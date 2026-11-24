package cl.duoc.albalab.contenidos.controller;

import cl.duoc.albalab.contenidos.model.DestinoAporte;
import cl.duoc.albalab.contenidos.repository.DestinoAporteRepository;
import cl.duoc.albalab.contenidos.service.ContenidoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/destinos")
public class DestinoAporteController extends ContenidoController<DestinoAporte> {
    public DestinoAporteController(DestinoAporteRepository repository) {
        super(new ContenidoService<>(repository, "El destino de aporte"));
    }
}
