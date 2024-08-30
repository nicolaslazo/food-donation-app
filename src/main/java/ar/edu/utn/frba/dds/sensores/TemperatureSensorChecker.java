package ar.edu.utn.frba.dds.sensores;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.TipoIncidente;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


public class TemperatureSensorChecker {
  private static CargarAlertaEnIncidentes cargadorIncidente;

  //Esto es para el TEST, pq no me agarra el Cargador de incidentes mockeado.
  public TemperatureSensorChecker(CargarAlertaEnIncidentes cargadorIncidente) {
    TemperatureSensorChecker.cargadorIncidente = cargadorIncidente;
  }

  public static void main(String[] args) throws CheckerException {
    HeladerasRepository heladerasRepository = new HeladerasRepository();

    List<Heladera> heladeras = heladerasRepository.getHeladerasConTemperaturaDesactualizada(5).toList();

    if (heladeras.isEmpty()) {
      throw new CheckerException("No hay heladeras disponibles para realizar el chequeo");
    }

    for (Heladera heladera : heladeras) {
      ZonedDateTime now = ZonedDateTime.now();
      ZonedDateTime lastTemperatureUpdate = heladera.getMomentoUltimaTempRegistrada();

      if (lastTemperatureUpdate != null && ChronoUnit.MINUTES.between(lastTemperatureUpdate, now) > 5) {
        cargadorIncidente.cargarIncidente(
            TipoIncidente.FALLA_CONEXION, heladera
        );

      }
    }
  }
}
