package ar.edu.utn.frba.dds.sensores;

import ar.edu.utn.frba.dds.dtos.input.sensores.SensorTemperaturaInputDTO;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.services.MqttBrokerService;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.time.ZonedDateTime;


public class SensorTemperatura implements IMqttMessageListener {
  private ReceptorTemperatura receptorTemperatura;
  private Heladera heladera;

  public SensorTemperatura(ReceptorTemperatura receptorTemperatura, Heladera heladera) throws MqttException {
    this.receptorTemperatura = receptorTemperatura;
    this.heladera = heladera;
    MqttBrokerService.getInstancia().suscribir("heladera/temperatura", this);
  }

  public void recibirDatos(double temperatura, ZonedDateTime momentoEvento) {
    this.receptorTemperatura.evaluarReceptor(temperatura, this.heladera, momentoEvento);
  }

  @Override
  public void messageArrived(String topic, MqttMessage message) {
    SensorTemperaturaInputDTO mensaje = SensorTemperaturaInputDTO.desdeJson(new String(message.getPayload()));
    this.recibirDatos(mensaje.getTemperatura(), mensaje.getTiempo());
  }
}
