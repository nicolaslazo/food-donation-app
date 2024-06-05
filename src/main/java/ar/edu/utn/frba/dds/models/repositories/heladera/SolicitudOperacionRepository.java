package ar.edu.utn.frba.dds.models.repositories.heladera;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudOperacion;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SolicitudOperacionRepository {
  static SolicitudOperacionRepository instancia = null;
  final List<SolicitudOperacion> solicitudes;

  private SolicitudOperacionRepository() {
    solicitudes = new ArrayList<>();
  }

  public static SolicitudOperacionRepository getInstancia() {
    if (instancia == null) {
      instancia = new SolicitudOperacionRepository();
    }

    return instancia;
  }

  public Optional<SolicitudOperacion> getSolicitudVigente(Colaborador colaborador, Heladera heladera) {
    ZonedDateTime ahora = ZonedDateTime.now();

    return solicitudes
        .stream()
        .filter(solicitud -> solicitud.getColaborador() == colaborador &&
            solicitud.getHeladera() == heladera &&
            solicitud.getFechaCreacion().isBefore(ahora) &&
            solicitud.getFechaVencimiento().isAfter(ahora))
        .findFirst();
  }

  public int insert(SolicitudOperacion solicitud) {
    solicitudes.add(solicitud);
    solicitud.setId(solicitudes.size());

    return solicitud.getId();
  }

  public void deleteTodas() {
    solicitudes.clear();
  }
}
