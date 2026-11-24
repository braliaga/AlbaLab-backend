package cl.duoc.albalab.contenidos.controller;

import cl.duoc.albalab.contenidos.model.Producto;
import cl.duoc.albalab.contenidos.repository.ProductoRepository;
import cl.duoc.albalab.contenidos.service.ContenidoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/productos")
public class ProductoController extends ContenidoController<Producto> {
    public ProductoController(ProductoRepository repository) {
        super(new ContenidoService<>(repository, "El producto"));
    }
}
