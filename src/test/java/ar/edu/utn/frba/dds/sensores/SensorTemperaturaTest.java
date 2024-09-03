package ar.edu.utn.frba.dds.sensores;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import ar.edu.utn.frba.dds.sensores.comandos.ActualizarRepositoryHeladera;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.ZonedDateTime;

class SensorTemperaturaTest {
  final ReceptorTemperatura receptorTemperaturaMock = Mockito.mock(ReceptorTemperatura.class);
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
  final AccionadorHeladera accionadorHeladera = new AccionadorHeladera();

  @BeforeEach
  void setUp() {
    new HeladerasRepository().insert(heladera);

    accionadorHeladera.agregarComando(
        new ActualizarRepositoryHeladera()
    );
  }

  @AfterEach
  void tearDown() {
    new HeladerasRepository().deleteAll();
  }

  // TODO: Arreglar antes de mergear
//  @Test
//  void recibirDatosCorrectos() throws MqttException {
//    SensorTemperatura sensor = new SensorTemperatura(
//        receptorTemperaturaMock,
//        heladera
//    );
//    sensor.recibirDatos(0.0, ZonedDateTime.now());
//    assertEquals(0.0, heladera.getUltimaTempRegistradaCelsius());
//  }
}
