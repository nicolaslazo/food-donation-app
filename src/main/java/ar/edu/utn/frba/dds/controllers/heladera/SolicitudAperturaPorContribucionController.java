package ar.edu.utn.frba.dds.controllers.heladera;

import ar.edu.utn.frba.dds.controllers.SuscripcionController;
import ar.edu.utn.frba.dds.dtos.input.heladera.SolicitudAperturaPorContribucionInputDTO;
import ar.edu.utn.frba.dds.dtos.output.heladera.SolicitudAperturaPorContribucionOutputDTO;
import ar.edu.utn.frba.dds.models.entities.contribucion.MovimientoViandas;
import ar.edu.utn.frba.dds.models.entities.contribucion.RedistribucionViandas;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudAperturaPorContribucion;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudInvalidaException;
import ar.edu.utn.frba.dds.models.entities.users.PermisoDenegadoException;
import ar.edu.utn.frba.dds.models.repositories.heladera.SolicitudAperturaPorContribucionRepository;
import ar.edu.utn.frba.dds.services.MqttBrokerService;
import lombok.NonNull;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.time.ZonedDateTime;
import java.util.Optional;

public class SolicitudAperturaPorContribucionController implements IMqttMessageListener {
  final SolicitudAperturaPorContribucionRepository repositorio =
      SolicitudAperturaPorContribucionRepository.getInstancia();

  private void checkearPrecondicionesCreacion(Tarjeta tarjeta, MovimientoViandas contribucion)
      throws PermisoDenegadoException {
    tarjeta.assertTienePermiso("depositarViandas",
        "las viandas sólo pueden ser ingresadas o redistribuidas por colaboradores registrados");

    if (contribucion.getColaborador().getUbicacion() == null)
      throw new PermisoDenegadoException(
          "El colaborador debe tener el domicilio registrado para hacer este tipo de contribuciones");

    if (tarjeta.getRecipiente() != contribucion.getColaborador().getUsuario())
      // Debería ser imposible que pase porque nosotros controlamos estas llamadas pero mejor estar seguros
      throw new PermisoDenegadoException("Un usuario no puede solicitar una apertura con la tarjeta de otro");
  }

  public void crear(@NonNull Tarjeta tarjeta,
                    @NonNull MovimientoViandas contribucion) throws MqttException, PermisoDenegadoException {
    checkearPrecondicionesCreacion(tarjeta, contribucion);

    SolicitudAperturaPorContribucion solicitud = new SolicitudAperturaPorContribucion(
        tarjeta,
        contribucion,
        ZonedDateTime.now());

    SolicitudAperturaPorContribucionRepository.getInstancia().insert(solicitud);

    String formatoTopicDeSolicitudes = "heladeras/%d/solicitudes";

    // TODO: Checkear que esto puede enviar y recibir mensajes
    // Este broker era un atributo de instancia y lo movimos acá para poder instanciar sin contactarnos con internet
    MqttBrokerService broker = MqttBrokerService.getInstancia();

    String dtoSolicitud = new SolicitudAperturaPorContribucionOutputDTO(solicitud).enJson();

    if (contribucion instanceof RedistribucionViandas) {
      String topicDeSolicitudesHeladeraOrigen =
          formatoTopicDeSolicitudes.formatted(((RedistribucionViandas) contribucion).getOrigen().getId());

      broker.publicar(topicDeSolicitudesHeladeraOrigen, dtoSolicitud);
    }

    String topicDeSolicitudesHeladeraDestino = formatoTopicDeSolicitudes.formatted(contribucion.getDestino().getId());

    broker.publicar(topicDeSolicitudesHeladeraDestino,
        dtoSolicitud);

    // Para marcar las solicitudes de apertura como usadas
    broker.suscribir(topicDeSolicitudesHeladeraDestino + "/confirmadas", this);
    // Para mandar las notificaciones necesarias a los suscriptos
    broker.suscribir(topicDeSolicitudesHeladeraDestino + "/confirmadas", SuscripcionController.getInstancia());
  }

  @Override
  public void messageArrived(String topic, MqttMessage payload) throws Exception {
    final SolicitudAperturaPorContribucionInputDTO confirmacion =
        SolicitudAperturaPorContribucionInputDTO.desdeJson(payload.toString());

    Optional<SolicitudAperturaPorContribucion> optionalSolicitud =
        repositorio.getSolicitudVigenteAlMomento(confirmacion.getId(),
            confirmacion.getEsExtraccion(),
            confirmacion.getFechaRealizada());

    if (optionalSolicitud.isEmpty())
      throw new SolicitudInvalidaException("La optionalSolicitud especificada no está vigente");

    SolicitudAperturaPorContribucion solicitud = optionalSolicitud.get();

    repositorio.updateFechaUsada(solicitud.getId(), confirmacion.getEsExtraccion(), confirmacion.getFechaRealizada());
  }
}
