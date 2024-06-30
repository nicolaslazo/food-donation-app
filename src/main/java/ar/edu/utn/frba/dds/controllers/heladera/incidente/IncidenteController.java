package ar.edu.utn.frba.dds.controllers.heladera.incidente;

import ar.edu.utn.frba.dds.dtos.input.heladera.incidente.IncidenteInputDTO;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.Incidente;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.TipoIncidente;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.incidente.IncidentesRepository;
import ar.edu.utn.frba.dds.services.MqttBrokerService;
import lombok.NonNull;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Optional;

public class IncidenteController implements IMqttMessageListener {
  final IncidentesRepository repositorio = IncidentesRepository.getInstancia();
  final MqttBrokerService brokerService = MqttBrokerService.getInstancia();

  public IncidenteController() throws MqttException {
    brokerService.suscribir("heladeras/incidentes", this);
  }

  private void crearAlerta(@NonNull Heladera heladera, @NonNull TipoIncidente tipo, @NonNull ZonedDateTime fecha) {
    repositorio.insert(new Incidente(heladera, tipo, fecha));
  }

  public void crearReporteDeFalla(@NonNull Heladera heladera,
                                  @NonNull ZonedDateTime fecha,
                                  @NonNull Colaborador colaborador,
                                  String descripcion,
                                  URL foto) {
    repositorio.insert(
        new Incidente(heladera, TipoIncidente.FALLA_REPORTADA_POR_COLABORADOR, fecha, colaborador, descripcion, foto));
  }

  @Override
  public void messageArrived(String topic, MqttMessage payload) throws Exception {
    IncidenteInputDTO mensaje = IncidenteInputDTO.desdeJson(payload.toString());

    Optional<Heladera> optionalHeladera = HeladerasRepository.getInstancia().get(mensaje.idHeladera());
    if (optionalHeladera.isEmpty()) throw new Exception("La heladera correspondiente a esta alerta no existe");

    crearAlerta(optionalHeladera.get(), TipoIncidente.fromString(mensaje.tipoIncidente()), mensaje.getFecha());
  }
}
