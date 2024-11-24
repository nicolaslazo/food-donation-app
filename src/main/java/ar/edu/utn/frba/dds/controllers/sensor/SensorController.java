package ar.edu.utn.frba.dds.controllers.sensor;

import ar.edu.utn.frba.dds.config.ConfigLoader;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.sensor.SensorMovimiento;
import ar.edu.utn.frba.dds.models.entities.heladera.sensor.SensorTemperatura;
import ar.edu.utn.frba.dds.models.repositories.heladera.sensor.SensorMovimientoRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.sensor.SensorTemperaturaRepository;
import ar.edu.utn.frba.dds.sensores.AccionadorHeladera;
import ar.edu.utn.frba.dds.sensores.ReceptorMovimiento;
import ar.edu.utn.frba.dds.sensores.ReceptorTemperatura;
import ar.edu.utn.frba.dds.services.MqttBrokerService;
import org.eclipse.paho.client.mqttv3.MqttException;

// Tópicos:
//  Para temperatura: heladera/{id}/temperatura.
//  Para movimiento: heladera/{id}/movimiento.
public class SensorController {
  public void create(Heladera heladera) throws MqttException {
    // Instancio los Sensores
    SensorMovimiento sensorMovimiento = asignarSensorMovimiento(heladera);
    SensorTemperatura sensorTemperatura = asignarSensorTemperatura(heladera);

    // Persisto los Sensores
    new SensorMovimientoRepository().insert(sensorMovimiento);
    new SensorTemperaturaRepository().insert(sensorTemperatura);
  }

  private SensorTemperatura asignarSensorTemperatura(Heladera heladera) throws MqttException {
    ReceptorTemperatura receptorTemperatura = new ReceptorTemperatura(
            Double.parseDouble(ConfigLoader.getInstancia().getProperty("temperatura_minima")),
            Double.parseDouble(ConfigLoader.getInstancia().getProperty("temperatura_maxima")),
            new AccionadorHeladera()
    );

    SensorTemperatura sensorTemperatura = new SensorTemperatura(
            receptorTemperatura,
            heladera
    );

    String topic = "heladera/" + heladera.getId() + "/temperatura";
    MqttBrokerService.getInstancia().suscribir(topic, sensorTemperatura);

    return sensorTemperatura;
  }

  private SensorMovimiento asignarSensorMovimiento(Heladera heladera) throws MqttException {
    ReceptorMovimiento receptorMovimiento = new ReceptorMovimiento(
            new AccionadorHeladera()
    );

    SensorMovimiento sensorMovimiento = new SensorMovimiento(
            receptorMovimiento,
            heladera
    );

    String topic = "heladera/" + heladera.getId() + "/movimiento";
    MqttBrokerService.getInstancia().suscribir(topic, sensorMovimiento);

    return sensorMovimiento;
  }

}
