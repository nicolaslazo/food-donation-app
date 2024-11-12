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
import org.eclipse.paho.client.mqttv3.MqttException;

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
    // Instancio un Receptor con las Temperaturas Mínimas
    // Opte por tener solo un Rango de Temperaturas porque no cambian según el tamaño de la heladera.
    ReceptorTemperatura receptorTemperatura = new ReceptorTemperatura(
            Double.parseDouble(ConfigLoader.getInstancia().getProperty("temperatura_minima")),
            Double.parseDouble(ConfigLoader.getInstancia().getProperty("temperatura_maxima")),
            new AccionadorHeladera()
    );
    return new SensorTemperatura(
            receptorTemperatura,
            heladera
    );
  }

  private SensorMovimiento asignarSensorMovimiento(Heladera heladera) throws MqttException {
    ReceptorMovimiento receptorMovimiento = new ReceptorMovimiento(
            new AccionadorHeladera()
    );
    return new SensorMovimiento(
            receptorMovimiento,
            heladera
    );
  }
}
