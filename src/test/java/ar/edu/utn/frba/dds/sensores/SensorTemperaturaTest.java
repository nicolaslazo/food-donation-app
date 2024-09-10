package ar.edu.utn.frba.dds.sensores;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.HibernatePersistenceReset;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.ZonedDateTime;

class SensorTemperaturaTest {
  final AccionadorHeladera accionadorHeladera = new AccionadorHeladera();
  final ReceptorTemperatura receptorTemperatura = new ReceptorTemperatura(5.0d,
      -5.0,
      accionadorHeladera);
  Colaborador colaborador;
  Heladera heladera;


  @BeforeEach
  void setUp() {
    colaborador = new Colaborador(new Documento(TipoDocumento.DNI, 1),
        "",
        "",
        LocalDate.now(),
        null);
    heladera = new Heladera(
        "Heladera Test",
        null,
        colaborador,
        10,
        ZonedDateTime.now().minusMonths(6),
        ""
    );

    new HeladerasRepository().insert(heladera);
  }

  @AfterEach
  void tearDown() {
    new HibernatePersistenceReset().execute();
  }

//  @Test
//  void recibirDatosCorrectos() throws MqttException {
//    SensorTemperatura sensor = new SensorTemperatura(
//        receptorTemperatura,
//        heladera
//    );
//    sensor.recibirDatos(0.0d, ZonedDateTime.now());
//    Optional<Heladera> heladeraReceptora = new HeladerasRepository().findById(heladera.getId());
//
//    assertTrue(heladeraReceptora.isPresent());
//    assertEquals(0.0d, heladeraReceptora.get().getUltimaTempRegistradaCelsius());
//  }
}
