package cl.duoc.albalab.contenidos.controller;

import cl.duoc.albalab.contenidos.model.Indicador;
import cl.duoc.albalab.contenidos.repository.IndicadorRepository;
import cl.duoc.albalab.contenidos.service.ContenidoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/indicadores")
public class IndicadorController extends ContenidoController<Indicador> {
    public IndicadorController(IndicadorRepository repository) {
        super(new ContenidoService<>(repository, "El indicador"));
    }
}
