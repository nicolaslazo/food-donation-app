package ar.edu.utn.frba.dds.sensores;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.users.UsuariosRepository;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SensorTemperaturaTest {
  final AccionadorHeladera accionadorHeladera = new AccionadorHeladera();
  final ReceptorTemperatura receptorTemperatura = new ReceptorTemperatura(5.0d,-5.0,accionadorHeladera);
  final Colaborador colaborador = new Colaborador(new Documento(TipoDocumento.DNI, 1),
      "",
      "",
      LocalDate.now(),
      null);
  final Heladera heladera = new Heladera(
      "Heladera Test",
      null,
      colaborador,
      10,
      ZonedDateTime.now().minusMonths(6)
  );

  @BeforeEach
  void setUp() {
    new HeladerasRepository().insert(heladera);
  }

  @AfterEach
  void tearDown() {
    new HeladerasRepository().deleteAll();
    new ColaboradorRepository().deleteAll();
    new UsuariosRepository().deleteAll();
  }

  @Test
  void recibirDatosCorrectos() throws MqttException {
    SensorTemperatura sensor = new SensorTemperatura(
        receptorTemperatura,
        heladera
    );
    sensor.recibirDatos(0.0d, ZonedDateTime.now());
    Optional<Heladera> heladeraReceptora = new HeladerasRepository().findById(heladera.getId());

    assertTrue(heladeraReceptora.isPresent());
    assertEquals(0.0d, heladeraReceptora.get().getUltimaTempRegistradaCelsius());
  }
}
