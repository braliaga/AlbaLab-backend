package cl.duoc.albalab.contenidos.config;

import cl.duoc.albalab.contenidos.model.CasoExito;
import cl.duoc.albalab.contenidos.model.DestinoAporte;
import cl.duoc.albalab.contenidos.model.Indicador;
import cl.duoc.albalab.contenidos.model.Producto;
import cl.duoc.albalab.contenidos.model.Programa;
import cl.duoc.albalab.contenidos.repository.CasoExitoRepository;
import cl.duoc.albalab.contenidos.repository.DestinoAporteRepository;
import cl.duoc.albalab.contenidos.repository.IndicadorRepository;
import cl.duoc.albalab.contenidos.repository.ProductoRepository;
import cl.duoc.albalab.contenidos.repository.ProgramaRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Carga el contenido inicial solo si la base está vacía. Son datos ficticios o anonimizados (propuesta técnica, punto 3).
@Configuration
public class DatosIniciales {
    @Bean
    public CommandLineRunner cargarContenido(ProgramaRepository programas, CasoExitoRepository casos,
                                             ProductoRepository productos, IndicadorRepository indicadores,
                                             DestinoAporteRepository destinos) {
        return args -> {
            if (programas.count() > 0) {
                return;
            }
            programas.saveAll(List.of(
                    new Programa(null, "Escuela Especial Jan Van Dijk",
                            "Educación especializada para niños con retos múltiples.",
                            "Desarrolla habilidades comunicativas y de autonomía funcional con acompañamiento directo a las familias."),
                    new Programa(null, "Talleres Sociolaborales Alba Lab",
                            "Preparación para el trabajo y elaboración de productos con sentido.",
                            "Espacio donde jóvenes crean papelería sustentable y regalos corporativos, fortaleciendo su inclusión."),
                    new Programa(null, "Vinculación Empresarial y RSE",
                            "Alianzas estratégicas, voluntariado corporativo y capacitación.",
                            "Asesoramos a organizaciones en cultura inclusiva y jornadas de voluntariado junto a nuestra comunidad.")));
            casos.saveAll(List.of(
                    new CasoExito(null, "Participante A (22 años)",
                            "Ingresó a los talleres de encuadernación adaptada tras egresar de la escuela.",
                            "Hoy lidera el control de calidad de sets para empresas aliadas."),
                    new CasoExito(null, "Participante B (25 años)",
                            "Se integró al programa de transición a la vida adulta.",
                            "Participa activamente presentando el catálogo Alba Lab."),
                    new CasoExito(null, "Empresa Colaboradora C",
                            "Reemplazó sus regalos anuales por kits elaborados en Alba Lab.",
                            "Financió insumos anuales para 15 participantes del taller.")));
            productos.saveAll(List.of(
                    new Producto(null, "Kit Corporativo Bienvenida",
                            "Libreta ecológica cosida a mano, lápiz plantable y bolsa.", 18500,
                            "https://picsum.photos/id/20/300/300"),
                    new Producto(null, "Set Papelería Alba Lab",
                            "Agenda perpetua en papel reciclado y separadores.", 12900,
                            "https://picsum.photos/id/24/300/300"),
                    new Producto(null, "Caja Regalo Inclusión RSE",
                            "Selección de productos artesanales con tarjeta de impacto.", 24900,
                            "https://picsum.photos/id/30/300/300")));
            indicadores.saveAll(List.of(
                    new Indicador(null, "+85", "Estudiantes en Escuela Jan Van Dijk"),
                    new Indicador(null, "+40", "Jóvenes activos en talleres"),
                    new Indicador(null, "24", "Alianzas empresariales RSE")));
            destinos.saveAll(List.of(
                    new DestinoAporte(null, "Materiales y equipamiento adaptado", "45%",
                            "Insumos para talleres productivos y ayudas técnicas sensoriales."),
                    new DestinoAporte(null, "Becas y apoyo terapéutico", "35%",
                            "Financiamiento directo para estudiantes y jóvenes con discapacidad múltiple."),
                    new DestinoAporte(null, "Inserción y seguimiento laboral", "20%",
                            "Acompañamiento por preparadores laborales en espacios de trabajo.")));
        };
    }
}
