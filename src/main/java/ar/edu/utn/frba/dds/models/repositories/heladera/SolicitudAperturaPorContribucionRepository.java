package ar.edu.utn.frba.dds.models.repositories.heladera;

import ar.edu.utn.frba.dds.models.entities.contribucion.MovimientoViandas;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudAperturaPorContribucion;

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

  public Optional<SolicitudAperturaPorContribucion> getSolicitudVigente(Tarjeta tarjeta, MovimientoViandas contribucion) {
    return solicitudes
        .stream()
        .filter(solicitud -> solicitud.getTarjeta() == tarjeta &&
            solicitud.getRazon() == contribucion &&
            solicitud.isVigente())
        .findFirst();
  }

  public int getCantidadViandasPendientes(Heladera heladera) {
    return solicitudes
        .stream()
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

  public void deleteTodas() {
    solicitudes.clear();
  }
}
