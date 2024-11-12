package ar.edu.utn.frba.dds.models.entities.heladera.sensor;

import ar.edu.utn.frba.dds.dtos.input.sensores.SensorTemperaturaInputDTO;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.sensores.ReceptorTemperatura;
import ar.edu.utn.frba.dds.services.MqttBrokerService;
import lombok.NonNull;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.ZonedDateTime;

@Entity
@Table(name = "sensorTemperatura")
public class SensorTemperatura implements IMqttMessageListener {
  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false, updatable = false, unique = true)
  @NonNull Long id;

  @Transient
  ReceptorTemperatura receptorTemperatura;

  @OneToOne(targetEntity = Heladera.class)
  @JoinColumn(name = "idHeladera", referencedColumnName = "id", nullable = false, updatable = false)
  @MapsId
  @NonNull Heladera heladera;

  public SensorTemperatura(ReceptorTemperatura receptorTemperatura, Heladera heladera) throws MqttException {
    this.receptorTemperatura = receptorTemperatura;
    this.heladera = heladera;
    MqttBrokerService.getInstancia().suscribir("heladera/temperatura", this);
  }

  protected SensorTemperatura() {}

  public void recibirDatos(double temperatura, ZonedDateTime momentoEvento) {
    this.receptorTemperatura.evaluarReceptor(temperatura, this.heladera, momentoEvento);
  }

  @Override
  public void messageArrived(String topic, MqttMessage message) {
    SensorTemperaturaInputDTO mensaje = SensorTemperaturaInputDTO.desdeJson(new String(message.getPayload()));
    this.recibirDatos(mensaje.getTemperatura(), mensaje.getTiempo());
  }
}
