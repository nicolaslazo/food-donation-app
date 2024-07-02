package ar.edu.utn.frba.dds.sensores;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import ar.edu.utn.frba.dds.sensores.comandos.ActualizarRepositoryHeladera;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SensorTemperaturaTest {
  final ReceptorTemperatura receptorTemperaturaMock = Mockito.mock(ReceptorTemperatura.class);
  final Heladera heladera = new Heladera(
      "Heladera Test",
      null,
      Mockito.mock(Colaborador.class),
      10,
      ZonedDateTime.now().minusMonths(6)
  );
  final AccionadorHeladera accionadorHeladera = new AccionadorHeladera();
  HeladerasRepository heladerasRepository = HeladerasRepository.getInstancia();

  @BeforeEach
  void setUp() throws RepositoryException {
    heladerasRepository.insert(heladera);

    accionadorHeladera.agregarComando(
        new ActualizarRepositoryHeladera()
    );
  }

  @AfterEach
  void tearDown() {
    heladerasRepository.deleteTodas();
  }

  @Test
  void recibirDatosCorrectos() throws MqttException {
    SensorTemperatura sensor = new SensorTemperatura(
        receptorTemperaturaMock,
        heladera
    );
    sensor.recibirDatos(0.0, ZonedDateTime.now());
    assertEquals(0.0, heladera.getUltimaTempRegistradaCelsius());
  }
}
