package ar.edu.utn.frba.dds.controllers.heladera;

import ar.edu.utn.frba.dds.controllers.SuscripcionController;
import ar.edu.utn.frba.dds.dtos.input.heladera.SolicitudAperturaPorConsumicionInputDTO;
import ar.edu.utn.frba.dds.dtos.output.heladera.SolicitudAperturaPorConsumicionOutputDTO;
import ar.edu.utn.frba.dds.models.entities.PersonaVulnerable;
import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudAperturaPorConsumicion;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudInvalidaException;
import ar.edu.utn.frba.dds.models.entities.users.PermisoDenegadoException;
import ar.edu.utn.frba.dds.models.repositories.PersonaVulnerableRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.SolicitudAperturaPorConsumicionRepository;
import ar.edu.utn.frba.dds.services.MqttBrokerService;
import lombok.NonNull;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;

public class SolicitudAperturaPorConsumicionController implements IMqttMessageListener {
  static final Logger logger = LoggerFactory.getLogger(SolicitudAperturaPorConsumicionController.class);

  final SolicitudAperturaPorConsumicionRepository repositorio = new SolicitudAperturaPorConsumicionRepository();

  private void checkearPrecondicionesCreacion(Tarjeta tarjeta, Vianda vianda)
      throws PermisoDenegadoException {
    tarjeta.assertTienePermiso("Abrir-Heladera-Consumicion",
        "Esta tarjeta no tiene permiso para solicitar viandas");

    PersonaVulnerable persona = new PersonaVulnerableRepository()
        .findById(tarjeta.getRecipiente().getId())
        .orElseThrow(() -> new PermisoDenegadoException("Esta tarjeta no corresponde a una persona vulnerable"));

    if (repositorio.findCantidadUsadasHoy(tarjeta) >= persona.getCantidadViandasPermitidasPorDia())
      throw new PermisoDenegadoException("Se alcanzó el límite de viandas extraídas por día");

    if (vianda.getHeladera() == null)
      throw new PermisoDenegadoException("Esta vianda no está almacenada en ninguna heladera");
  }

  public SolicitudAperturaPorConsumicion crear(@NonNull Tarjeta tarjeta,
                                               @NonNull Vianda vianda) throws MqttException, PermisoDenegadoException {
    checkearPrecondicionesCreacion(tarjeta, vianda);

    SolicitudAperturaPorConsumicion solicitud = new SolicitudAperturaPorConsumicion(
        tarjeta,
        vianda,
        ZonedDateTime.now());

    new SolicitudAperturaPorConsumicionRepository().insert(solicitud);

    // Este broker era un atributo de instancia y lo movimos acá para poder instanciar sin contactarnos con internet
    MqttBrokerService broker = MqttBrokerService.getInstancia();

    String dtoSolicitud = new SolicitudAperturaPorConsumicionOutputDTO(solicitud).enJson();

    String formatoTopicDeSolicitudes = "heladera/%d/solicitud/consumicion";
    String topicSolicitudes = formatoTopicDeSolicitudes.formatted(solicitud.getHeladera().getId());

    broker.publicar(topicSolicitudes, dtoSolicitud);

    /*
     * CUIDADO: SUSCRIBIR DOS TOPICS HACE QUE INTELLIJ NO CAPTURE ERRORES NI PUEDA FRENAR EN BREAKPOINTS
     * SI NECESITAN DEBUGGEAR ALGO COMENTEN LOS `broker.suscribir` QUE NO NECESITEN
     */
    // Para marcar las solicitudes de apertura como usadas
    broker.suscribir(topicSolicitudes + "/confirmada", this);
    // Para mandar las notificaciones necesarias a los suscriptos
    broker.suscribir(topicSolicitudes + "/confirmada", SuscripcionController.getInstancia());

    return solicitud;
  }

  @Override
  public void messageArrived(String topic, MqttMessage payload) throws Exception {
    logger.warn("Payload recibida: " + payload);

    final SolicitudAperturaPorConsumicionInputDTO confirmacion =
        SolicitudAperturaPorConsumicionInputDTO.desdeJson(payload.toString());

    logger.warn("Confirmacion: " + confirmacion);

    SolicitudAperturaPorConsumicion solicitud = repositorio
        .getSolicitudVigenteAlMomento(Long.valueOf(confirmacion.getId()), confirmacion.getFechaRealizada())
        .orElseThrow(() -> new SolicitudInvalidaException("La solicitud especificada no está vigente"));

    repositorio.updateFechaUsada(solicitud.getId(), confirmacion.getFechaRealizada());

    logger.warn("Solicitud usada exitosamente el " + solicitud.getFechaUsada());
  }
}
