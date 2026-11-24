package cl.duoc.albalab.contenidos.controller;

import cl.duoc.albalab.contenidos.model.Programa;
import cl.duoc.albalab.contenidos.repository.ProgramaRepository;
import cl.duoc.albalab.contenidos.service.ContenidoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/programas")
public class ProgramaController extends ContenidoController<Programa> {
    public ProgramaController(ProgramaRepository repository) {
        super(new ContenidoService<>(repository, "El programa"));
    }
}
