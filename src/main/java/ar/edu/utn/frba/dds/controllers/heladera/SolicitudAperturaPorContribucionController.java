package ar.edu.utn.frba.dds.controllers.heladera;

import ar.edu.utn.frba.dds.dtos.input.heladera.SolicitudAperturaPorContribucionInputDTO;
import ar.edu.utn.frba.dds.dtos.output.heladera.SolicitudAperturaPorContribucionOutputDTO;
import ar.edu.utn.frba.dds.models.entities.contribucion.ContribucionYaRealizadaException;
import ar.edu.utn.frba.dds.models.entities.contribucion.MovimientoViandas;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudAperturaPorContribucion;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudInvalidaException;
import ar.edu.utn.frba.dds.models.entities.users.PermisoDenegadoException;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
import ar.edu.utn.frba.dds.models.repositories.ViandasRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.SolicitudAperturaPorContribucionRepository;
import ar.edu.utn.frba.dds.services.MqttBrokerService;
import lombok.NonNull;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.time.ZonedDateTime;
import java.util.Optional;

public class SolicitudAperturaPorContribucionController implements IMqttMessageListener {
  final MqttBrokerService brokerService = MqttBrokerService.getInstancia();
  final SolicitudAperturaPorContribucionRepository repositorio =
      SolicitudAperturaPorContribucionRepository.getInstancia();

  public SolicitudAperturaPorContribucionController() throws MqttException {
  }

  private void checkearPrecondicionesCreacion(Tarjeta tarjeta, MovimientoViandas contribucion) {
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
                    @NonNull MovimientoViandas contribucion) throws MqttException {
    checkearPrecondicionesCreacion(tarjeta, contribucion);

    SolicitudAperturaPorContribucion solicitud = new SolicitudAperturaPorContribucion(
        tarjeta,
        contribucion,
        ZonedDateTime.now());

    SolicitudAperturaPorContribucionRepository.getInstancia().insert(solicitud);

    String topicDeSolicitudes = "heladeras/" + contribucion.getDestino().getId() + "/solicitudes";

    brokerService.publicar(topicDeSolicitudes,
        new SolicitudAperturaPorContribucionOutputDTO(solicitud).aJson());
    brokerService.suscribir(topicDeSolicitudes + "/confirmadas", this);
  }

  @Override
  public void messageArrived(String topic, MqttMessage payload)
      throws SolicitudInvalidaException, RepositoryException, ContribucionYaRealizadaException {
    final SolicitudAperturaPorContribucionInputDTO confirmacion =
        SolicitudAperturaPorContribucionInputDTO.desdeJson(payload.toString());

    Optional<SolicitudAperturaPorContribucion> optionalSolicitud =
        repositorio.getSolicitudVigenteAlMomento(confirmacion.id(), confirmacion.getFechaRealizada());

    if (optionalSolicitud.isEmpty())
      throw new SolicitudInvalidaException("La optionalSolicitud especificada no está vigente");

    SolicitudAperturaPorContribucion solicitud = optionalSolicitud.get();

    repositorio.updateFechaUsada(solicitud.getId(), confirmacion.getFechaRealizada());
    solicitud.getRazon().setFechaRealizada(confirmacion.getFechaRealizada());
    ViandasRepository
        .getInstancia()
        .updateUbicacion(solicitud.getRazon().getViandas(), solicitud.getRazon().getDestino());
  }
}
