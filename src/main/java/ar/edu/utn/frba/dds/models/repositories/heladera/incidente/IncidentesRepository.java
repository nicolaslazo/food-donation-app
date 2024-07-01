package ar.edu.utn.frba.dds.models.repositories.heladera.incidente;

import ar.edu.utn.frba.dds.models.entities.heladera.incidente.Incidente;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.TipoIncidente;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class IncidentesRepository {
  private static IncidentesRepository instancia = null;
  final List<Incidente> incidentes;

  private IncidentesRepository() {
    incidentes = new ArrayList<>();
  }

  public static IncidentesRepository getInstancia() {
    if (instancia == null) instancia = new IncidentesRepository();
    return instancia;
  }

  public Optional<Incidente> get(int id) {
    return incidentes.stream().filter(incidente -> incidente.getId() == id).findFirst();
  }

  public int insert(Incidente incidente) {
    incidentes.add(incidente);
    incidente.setId(incidentes.size());

    return incidente.getId();
  }

  public List<Incidente> filtrarIncidentesDesdeSemanaPasada() {
    ZonedDateTime unaSemanaAtras = ZonedDateTime.now().minus(1, ChronoUnit.WEEKS);

    return incidentes.stream()
        .filter(incidente -> incidente.getFecha().isAfter(unaSemanaAtras))
        .collect(Collectors.toList());
  }


  public void deleteTodos() {
    incidentes.clear();
  }

}