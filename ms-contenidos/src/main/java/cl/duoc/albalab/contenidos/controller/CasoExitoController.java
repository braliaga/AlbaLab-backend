package cl.duoc.albalab.contenidos.controller;

import cl.duoc.albalab.contenidos.model.CasoExito;
import cl.duoc.albalab.contenidos.repository.CasoExitoRepository;
import cl.duoc.albalab.contenidos.service.ContenidoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/casos")
public class CasoExitoController extends ContenidoController<CasoExito> {
    public CasoExitoController(CasoExitoRepository repository) {
        super(new ContenidoService<>(repository, "El caso de éxito"));
    }
}
