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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.Optional;

public class SolicitudAperturaPorContribucionController implements IMqttMessageListener {
  static final Logger logger = LoggerFactory.getLogger(SolicitudAperturaPorContribucionController.class);

  final SolicitudAperturaPorContribucionRepository repositorio = new SolicitudAperturaPorContribucionRepository();

  private void checkearPrecondicionesCreacion(Tarjeta tarjeta, MovimientoViandas contribucion)
      throws PermisoDenegadoException {
    tarjeta.assertTienePermiso("Donar-Viandas",
        "las viandas sólo pueden ser ingresadas o redistribuidas por colaboradores registrados");

    if (contribucion.getColaborador().getUbicacion() == null)
      throw new PermisoDenegadoException(
          "El colaborador debe tener el domicilio registrado para hacer este tipo de contribuciones");

    if (tarjeta.getRecipiente() != contribucion.getColaborador().getUsuario())
      // Debería ser imposible que pase porque nosotros controlamos estas llamadas pero mejor estar seguros
      throw new PermisoDenegadoException("Un usuario no puede solicitar una apertura con la tarjeta de otro");
  }

  public SolicitudAperturaPorContribucion crear(@NonNull Tarjeta tarjeta,
                                                @NonNull MovimientoViandas contribucion) throws MqttException, PermisoDenegadoException {
    checkearPrecondicionesCreacion(tarjeta, contribucion);

    SolicitudAperturaPorContribucion solicitud = new SolicitudAperturaPorContribucion(
        tarjeta,
        contribucion,
        ZonedDateTime.now());

    repositorio.insert(solicitud);

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

    /*
     * CUIDADO: SUSCRIBIR DOS TOPICS HACE QUE INTELLIJ NO CAPTURE ERRORES NI PUEDA FRENAR EN BREAKPOINTS
     * SI NECESITAN DEBUGGEAR ALGO COMENTEN LOS `broker.suscribir` QUE NO NECESITEN
     */
    // Para marcar las solicitudes de apertura como usadas
    broker.suscribir(topicDeSolicitudesHeladeraDestino + "/confirmadas", this);
    // Para mandar las notificaciones necesarias a los suscriptos
    broker.suscribir(topicDeSolicitudesHeladeraDestino + "/confirmadas", SuscripcionController.getInstancia());

    return solicitud;
  }

  @Override
  public void messageArrived(String topic, MqttMessage payload) throws Exception {
    logger.warn("Payload recibida: " + payload);

    final SolicitudAperturaPorContribucionInputDTO confirmacion =
        SolicitudAperturaPorContribucionInputDTO.desdeJson(payload.toString());

    logger.warn("Confirmacion: " + confirmacion);

    Optional<SolicitudAperturaPorContribucion> optionalSolicitud =
        repositorio.getSolicitudVigenteAlMomento(Long.valueOf(confirmacion.getId()),
            confirmacion.getEsExtraccion(),
            confirmacion.getFechaRealizada());

    if (optionalSolicitud.isEmpty())
      throw new SolicitudInvalidaException("La optionalSolicitud especificada no está vigente");

    SolicitudAperturaPorContribucion solicitud = optionalSolicitud.get();

    repositorio.updateFechaUsada(solicitud.getId(), confirmacion.getEsExtraccion(), confirmacion.getFechaRealizada());

    logger.warn("Solicitud usada exitosamente el " + solicitud.getFechaAperturaEnDestino());
  }
}
