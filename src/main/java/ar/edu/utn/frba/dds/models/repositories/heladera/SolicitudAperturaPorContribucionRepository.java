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

  public Optional<SolicitudAperturaPorContribucion> getSolicitudVigenteAlMomento(int id,
                                                                                 boolean paraExtraccion,
                                                                                 ZonedDateTime momento) {
    return solicitudes
        .stream()
        .filter(solicitud -> (solicitud.getId() == id) && (solicitud.isVigenteAlMomento(momento, paraExtraccion)))
        .findFirst();
  }

  public Optional<SolicitudAperturaPorContribucion> getSolicitudVigente(int id, boolean paraExtraccion) {
    return getSolicitudVigenteAlMomento(id, paraExtraccion, ZonedDateTime.now());
  }

  public int getCantidadViandasPendientes(Heladera heladera) {
    return solicitudes
        .stream()
        .filter(solicitud -> solicitud.isVigente(true) || solicitud.isVigente(false))
        .filter(solicitud -> solicitud.getHeladeraDestino().getId() == heladera.getId())
        .mapToInt(solicitud -> solicitud.getViandas().size())
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

  public void updateFechaUsada(int id, boolean paraExtraccion, ZonedDateTime fechaUsada)
      throws SolicitudInvalidaException {
    Optional<SolicitudAperturaPorContribucion> optionalSolicitud =
        getSolicitudVigenteAlMomento(id, paraExtraccion, fechaUsada);

    if (optionalSolicitud.isEmpty()) {
      String operacion = paraExtraccion ? "extracción" : "depósito";

      throw new SolicitudInvalidaException(
          "No existe solicitud vigente con id %d para %s de viandas".formatted(id, operacion));
    }

    if (paraExtraccion) {
      optionalSolicitud.get().setFechaAperturaEnOrigen(fechaUsada);
    } else {
      optionalSolicitud.get().setFechaAperturaEnDestino(fechaUsada);
    }
  }

  public void deleteTodas() {
    solicitudes.clear();
  }
}
