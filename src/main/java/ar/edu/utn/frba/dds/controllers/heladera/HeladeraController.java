package ar.edu.utn.frba.dds.controllers.heladera;

import ar.edu.utn.frba.dds.config.ConfigLoader;
import ar.edu.utn.frba.dds.controllers.heladera.incidente.IncidenteController;
import ar.edu.utn.frba.dds.models.entities.Tecnico;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.TipoIncidente;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CalculadoraDistancia;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.models.repositories.TecnicoRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HeladeraController {
  final HeladerasRepository repositorio = HeladerasRepository.getInstancia();
  final IncidenteController incidenteController = IncidenteController.getInstancia();

  public static void main(String[] args) {
    final HeladeraController controller = new HeladeraController();

    controller.getHeladerasConFallaDeConexion().forEach(controller::alertarFallaDeConexion);
  }

  public static Optional<Tecnico> encontrarTecnicoMasCercano(Heladera heladera) {
    final Ubicacion coordsHeladera = heladera.getUbicacion();

    HashMap<Tecnico, Double> distanciasAHeladera = new HashMap<>();
    TecnicoRepository
        .getInstancia()
        .getTecnicos()
        .stream()
        .filter(tecnico -> tecnico.isDentroDeRango(heladera))
        .forEach(tecnico ->
            distanciasAHeladera.put(
                tecnico,
                CalculadoraDistancia.calcular(tecnico.getAreaAsignada().centro(), coordsHeladera)
            )
        );

    return distanciasAHeladera
        .entrySet()
        .stream()
        .reduce((una, otra) -> una.getValue() < otra.getValue() ? una : otra)
        .map(Map.Entry::getKey);
  }

  public List<Heladera> encontrarHeladerasCercanas(Heladera target) {
    Ubicacion ubicacionTarget = target.getUbicacion();

    return repositorio
        .getTodas()
        .stream()
        .sorted(Comparator.comparing(unaHeladera ->
            CalculadoraDistancia.calcular(unaHeladera.getUbicacion(), ubicacionTarget)))
        .skip(1) // Para eliminar al target en sí
        .limit(3)
        .toList();
  }

  public Collection<Heladera> getHeladerasConFallaDeConexion() {
    final int minutosDeSilencio =
        Integer.parseInt(ConfigLoader.getInstancia().getProperty("heladeras.sensores.limiteDeEsperaEnMinutos"));

    return repositorio.getHeladerasConTemperaturaDesactualizada(minutosDeSilencio);
  }

  public void alertarFallaDeConexion(Heladera heladera) {
    incidenteController.crearAlerta(heladera, TipoIncidente.FALLA_CONEXION, ZonedDateTime.now());
  }
}
