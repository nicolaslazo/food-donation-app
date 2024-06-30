package ar.edu.utn.frba.dds.models.repositories.heladera.incidente;

import ar.edu.utn.frba.dds.models.entities.heladera.incidente.Incidente;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.VisitaTecnica;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VisitasTecnicasRepository {
  private static VisitasTecnicasRepository instancia = null;
  final List<VisitaTecnica> visitas;

  private VisitasTecnicasRepository() {
    visitas = new ArrayList<>();
  }

  public static VisitasTecnicasRepository getInstancia() {
    if (instancia == null) instancia = new VisitasTecnicasRepository();

    return instancia;
  }

  public Optional<VisitaTecnica> get(int id) {
    return visitas.stream().filter(visita -> visita.getId() == id).findFirst();
  }

  public boolean getIsResuelto(Incidente incidente) {
    return visitas.stream().anyMatch(visita -> visita.getIncidente() == incidente && visita.isIncidenteResuelto());
  }

  public int insert(VisitaTecnica visita) throws RepositoryException {
    if (getIsResuelto(visita.getIncidente()))
      throw new RepositoryException("No se pueden agregar visitas a incidentes resueltos");

    visitas.add(visita);
    visita.setId(visitas.size());

    return visita.getId();
  }

  public void deleteTodas() {
    visitas.clear();
  }
}
