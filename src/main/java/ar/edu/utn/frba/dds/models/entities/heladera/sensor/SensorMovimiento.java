package ar.edu.utn.frba.dds.models.entities.heladera.sensor;

import ar.edu.utn.frba.dds.dtos.input.sensores.SensorMovimientoInputDTO;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.sensores.ReceptorMovimiento;
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
@Table(name = "sensorMovimiento")
public class SensorMovimiento implements IMqttMessageListener {
  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false, updatable = false, unique = true)
  @NonNull Long id;

  @Transient
  ReceptorMovimiento receptorMovimiento;

  @OneToOne(targetEntity = Heladera.class)
  @JoinColumn(name = "idHeladera", referencedColumnName = "id", nullable = false, updatable = false)
  @MapsId
  @NonNull Heladera heladera;

  public SensorMovimiento(ReceptorMovimiento receptorMovimiento, Heladera heladera) throws MqttException {
    this.receptorMovimiento = receptorMovimiento;
    this.heladera = heladera;
  }

  protected SensorMovimiento() {}

  public void recibirDatos(ZonedDateTime momentoEvento) {
    receptorMovimiento.evaluarReceptor(momentoEvento, heladera);
  }

  @Override
  public void messageArrived(String topic, MqttMessage payload) {
    SensorMovimientoInputDTO mensaje = SensorMovimientoInputDTO.desdeJson(new String(payload.getPayload()));
    this.recibirDatos(mensaje.getTiempo());
  }
}
