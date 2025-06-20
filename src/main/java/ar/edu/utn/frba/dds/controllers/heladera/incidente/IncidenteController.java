package ar.edu.utn.frba.dds.controllers.heladera.incidente;

import ar.edu.utn.frba.dds.controllers.SuscripcionController;
import ar.edu.utn.frba.dds.controllers.heladera.HeladeraController;
import ar.edu.utn.frba.dds.dtos.input.heladera.incidente.IncidenteInputDTO;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.Incidente;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.TipoIncidente;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.incidente.IncidenteRepository;
import ar.edu.utn.frba.dds.services.MqttBrokerService;
import io.javalin.http.Context;
import lombok.NonNull;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class IncidenteController implements IMqttMessageListener {
  static IncidenteController instancia = null;
  // MqttBrokerService brokerService = MqttBrokerService.getInstancia();

  private IncidenteController() throws MqttException {
    // brokerService.suscribir("heladeras/incidentes", this);
  }

  public static IncidenteController getInstancia() {
    if (instancia == null) {
      try {
        instancia = new IncidenteController();
      } catch (MqttException e) {
        throw new RuntimeException(e);
      }
    }

    return instancia;
  }

  private void notificarAInteresados(Heladera heladera, ZonedDateTime fecha) {
    SuscripcionController.notificarIncidente(heladera, fecha);
    HeladeraController.notificarTecnicoMasCercanoDeIncidente(heladera, fecha);
  }

  public void crearAlerta(@NonNull Heladera heladera, @NonNull TipoIncidente tipo, @NonNull ZonedDateTime fecha) {
    new IncidenteRepository().insert(new Incidente(heladera, tipo, fecha));

    notificarAInteresados(heladera, fecha);
  }

  public void crearReporteDeFalla(@NonNull Heladera heladera,
                                  @NonNull ZonedDateTime fecha,
                                  @NonNull Colaborador colaborador,
                                  String descripcion,
                                  String pathImagen) {
    new IncidenteRepository().insert(
        new Incidente(heladera, TipoIncidente.FALLA_REPORTADA_POR_COLABORADOR, fecha, colaborador, descripcion, pathImagen));

    notificarAInteresados(heladera, fecha);
  }

  @Override
  public void messageArrived(String topic, MqttMessage payload) throws Exception {
    IncidenteInputDTO mensaje = IncidenteInputDTO.desdeJson(payload.toString());

    Optional<Heladera> optionalHeladera = new HeladerasRepository().findById(mensaje.idHeladera());
    if (optionalHeladera.isEmpty()) throw new Exception("La heladera correspondiente a esta alerta no existe");

    crearAlerta(optionalHeladera.get(), TipoIncidente.fromString(mensaje.tipoIncidente()), mensaje.getFecha());
  }

  public void index(Context context) {
    Map<String, Object> model = context.attribute("model");
    List<Heladera> heladeras = new HeladerasRepository().findAll().toList();
    model.put("heladeras", heladeras);
    context.render("incidentes/falla-tecnica/falla-tecnica.hbs", model);
  }
}
