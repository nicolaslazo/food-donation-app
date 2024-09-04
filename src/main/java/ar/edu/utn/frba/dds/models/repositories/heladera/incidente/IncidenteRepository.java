package ar.edu.utn.frba.dds.models.repositories.heladera.incidente;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.Incidente;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class IncidenteRepository extends HibernateEntityManager<Incidente, Long> {
  private List<Incidente> incidentesHeladeras;

  public IncidenteRepository() {
    incidentesHeladeras = new ArrayList<>();
  }

  public Map<Heladera, Integer> getCantidadIncidentesPorHeladeraSemanaPasada() {
    ZonedDateTime haceUnaSemana = ZonedDateTime.now().minusWeeks(1);

    return incidentesHeladeras
        .stream()
        .filter(incidente -> incidente.getFecha().isAfter(haceUnaSemana))
        .collect(Collectors.groupingBy(
            Incidente::getHeladera,
            Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
        ));
  }


}
