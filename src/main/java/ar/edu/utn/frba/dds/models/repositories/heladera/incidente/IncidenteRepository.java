package ar.edu.utn.frba.dds.models.repositories.heladera.incidente;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.Incidente;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class IncidenteRepository {
  private static IncidenteRepository instancia = null;
  private List<Incidente> incidentesHeladeras;

  public IncidenteRepository() {
    incidentesHeladeras = new ArrayList<>();
  }

  public static IncidenteRepository getInstancia() {
    if (instancia == null) {
      instancia = new IncidenteRepository();
    }
    return instancia;
  }

  public Optional<Incidente> getIncidenteHeladera(int id) {
    return incidentesHeladeras
        .stream()
        .filter(inc -> inc.getId() == id)
        .findFirst();
  }

  public List<Incidente> getIncidenteHeladeras() {
    return incidentesHeladeras;
  }


  public void insertIncidenteHeladera(Incidente incidente) {
    incidente.setId(incidentesHeladeras.size() + 1);
    incidentesHeladeras.add(incidente);
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

  public int insert(Incidente incidente) {
    incidentesHeladeras.add(incidente);
    incidente.setId(incidentesHeladeras.size());

    return incidente.getId();
  }

  public boolean deleteIncidenteHeladera(int id) {
    Optional<Incidente> incidenteHeladera = getIncidenteHeladera(id);
    return incidenteHeladera.map(incidentesHeladeras::remove).orElse(false);
  }


  public boolean existsIncidenteHeladera(int id) {
    return getIncidenteHeladera(id).isPresent();
  }

  public void deleteTodos() {
    incidentesHeladeras.clear();
  }

}
