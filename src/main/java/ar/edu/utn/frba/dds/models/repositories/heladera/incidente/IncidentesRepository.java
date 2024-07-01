package ar.edu.utn.frba.dds.models.repositories.heladera.incidente;

import ar.edu.utn.frba.dds.models.entities.heladera.incidente.Incidente;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

  public void deleteTodos() {
    incidentes.clear();
  }
}
