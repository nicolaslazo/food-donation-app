package ar.edu.utn.frba.dds.controllers.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.MotivoDeDistribucion;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.repositories.HibernatePersistenceReset;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.contacto.SuscripcionRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CuidadoHeladeraControllerTest {
  Colaborador colaborador = new Colaborador(
      new Documento(TipoDocumento.DNI, 1),
      "",
      "",
      LocalDate.now(),
      new CoordenadasGeograficas(-34d, -58d));

  @BeforeEach
  void setUp() {
    new ColaboradorRepository().insert(colaborador);
  }

  @AfterEach
  void tearDown() {
    new HibernatePersistenceReset().execute();
  }

  @Test
  void testFallaSiColaboradorNoExiste() {
    assertThrows(NoSuchElementException.class,
        () -> CuidadoHeladeraController
            .tomarCuidadoHeladera(
                "{" +
                    "\"nombreHeladera\": \"Heladera\", " +
                    "\"modeloHeladera\": \"HELADERA_CHICA\", " +
                    "\"idColaborador\": \"" + 343434  + "\" , " +
                    "\"latitud\": -34.0, " +
                    "\"longitud\": -58.0}"));
  }

  @Test
  void testCreaHeladeraEnRepositorio() {
    Heladera heladeraNueva = CuidadoHeladeraController.tomarCuidadoHeladera(
        "{" +
            "\"nombreHeladera\": \"Heladera\", " +
            "\"modeloHeladera\": \"HELADERA_CHICA\", " +
            "\"idColaborador\": \"" + colaborador.getId() + "\", " +
            "\"latitud\": -34.0, " +
            "\"longitud\": -58.0, " +
            "\"barrio\": \"Medrano\"}"
    );

    assertTrue(new HeladerasRepository().findById(heladeraNueva.getId()).isPresent());
  }

  @Test
  void testCreaSuscripcionParaCuidador() {
    Heladera heladera = CuidadoHeladeraController.tomarCuidadoHeladera(
        "{" +
            "\"nombreHeladera\": \"Heladera\", " +
            "\"modeloHeladera\": \"HELADERA_CHICA\", " +
            "\"idColaborador\": \"" + colaborador.getId() + "\", " +
            "\"latitud\": -34.0, " +
            "\"longitud\": -58.0, " +
            "\"barrio\": \"Medrano\"}");

    assertTrue(
        new SuscripcionRepository().find(colaborador, heladera, MotivoDeDistribucion.FALLA_HELADERA).isPresent());
  }
}