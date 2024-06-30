package ar.edu.utn.frba.dds.models.repositories.heladera;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudAperturaPorContribucion;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudInvalidaException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SolicitudAperturaPorContribucionRepository {
  private static SolicitudAperturaPorContribucionRepository instancia = null;
  final List<SolicitudAperturaPorContribucion> solicitudes;

  private SolicitudAperturaPorContribucionRepository() {
    solicitudes = new ArrayList<>();
  }

  public static SolicitudAperturaPorContribucionRepository getInstancia() {
    if (instancia == null) {
      instancia = new SolicitudAperturaPorContribucionRepository();
    }

    return instancia;
  }

  public Optional<SolicitudAperturaPorContribucion> get(int id) {
    return solicitudes.stream().filter(solicitud -> solicitud.getId() == id).findFirst();
  }

  public Optional<SolicitudAperturaPorContribucion> getSolicitudVigenteAlMomento(int id, ZonedDateTime momento) {
    return solicitudes
        .stream()
        .filter(solicitud -> solicitud.getId() == id && solicitud.isVigenteAlMomento(momento))
        .findFirst();
  }

  public Optional<SolicitudAperturaPorContribucion> getSolicitudVigente(int id) {
    return getSolicitudVigenteAlMomento(id, ZonedDateTime.now());
  }

  public int getCantidadViandasPendientes(Heladera heladera) {
    return solicitudes
        .stream()
        .filter(SolicitudAperturaPorContribucion::isVigente)
        .filter(solicitud -> solicitud.getRazon().getDestino().getId() == heladera.getId())
        .mapToInt(solicitud -> solicitud.getRazon().getViandas().size())
        .sum();
  }

  public List<SolicitudAperturaPorContribucion> getTodas() {
    return solicitudes;
  }

  public int insert(SolicitudAperturaPorContribucion solicitud) {
    solicitudes.add(solicitud);
    solicitud.setId(solicitudes.size());

    return solicitud.getId();
  }

  public void updateFechaUsada(int id, ZonedDateTime fechaUsada) throws SolicitudInvalidaException {
    Optional<SolicitudAperturaPorContribucion> optionalSolicitud = getSolicitudVigenteAlMomento(id, fechaUsada);

    if (optionalSolicitud.isEmpty()) throw new SolicitudInvalidaException("No existe solicitud vigente con id " + id);

    optionalSolicitud.get().setFechaUsada(fechaUsada);
  }

  public void deleteTodas() {
    solicitudes.clear();
  }
}
