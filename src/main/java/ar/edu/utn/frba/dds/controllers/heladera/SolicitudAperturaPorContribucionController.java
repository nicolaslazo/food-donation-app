package ar.edu.utn.frba.dds.controllers.heladera;

import ar.edu.utn.frba.dds.dtos.output.heladera.SolicitudAperturaPorContribucionOutputDTO;
import ar.edu.utn.frba.dds.models.entities.contribucion.MovimientoViandas;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudAperturaPorContribucion;
import ar.edu.utn.frba.dds.models.entities.users.PermisoDenegadoException;
import ar.edu.utn.frba.dds.models.repositories.heladera.SolicitudAperturaPorContribucionRepository;
import ar.edu.utn.frba.dds.services.MqttBrokerService;
import lombok.NonNull;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.time.ZonedDateTime;

public class SolicitudAperturaPorContribucionController {
  public static void crear(@NonNull Tarjeta tarjeta, @NonNull MovimientoViandas contribucion) throws MqttException {
    checkearPrecondicionesCreacion(tarjeta, contribucion);

    SolicitudAperturaPorContribucion solicitud = new SolicitudAperturaPorContribucion(
        tarjeta,
        contribucion,
        ZonedDateTime.now());

    SolicitudAperturaPorContribucionRepository.getInstancia().insert(solicitud);

    MqttBrokerService
        .getInstancia()
        .publicar("heladeras/" + contribucion.getId() + "/solicitudes",
            new SolicitudAperturaPorContribucionOutputDTO(solicitud).aJson());
  }

  private static void checkearPrecondicionesCreacion(Tarjeta tarjeta, MovimientoViandas contribucion) {
    tarjeta.assertTienePermiso("depositarViandas",
            "las viandas sólo pueden ser ingresadas o redistribuidas por colaboradores registrados");

    if (tarjeta.getRecipiente() != contribucion.getColaborador().getUsuario())
      // Debería ser imposible que pase porque nosotros controlamos estas llamadas pero mejor estar seguros
      throw new PermisoDenegadoException("Un usuario no puede solicitar una apertura con la tarjeta de otro");
  }
}
