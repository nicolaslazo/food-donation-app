package ar.edu.utn.frba.dds.controllers.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.MotivoDeDistribucion;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.contacto.SuscripcionRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.users.UsuariosRepository;
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
    SuscripcionRepository.getInstancia().deleteTodas();
    new HeladerasRepository().deleteAll();
    new ColaboradorRepository().deleteAll();
    new UsuariosRepository().deleteAll();
  }

  @Test
  void testFallaSiColaboradorNoExiste() {
    assertThrows(NoSuchElementException.class,
        () -> CuidadoHeladeraController
            .tomarCuidadoHeladera(
                "{" +
                    "\"nombreHeladera\": \"Heladera\", " +
                    "\"capacidadEnViandas\": 1, " +
                    "\"idColaboradorSerializado\": \"00000000-0000-0000-0000-000000000000\", " +
                    "\"latitud\": -34.0, " +
                    "\"longitud\": -58.0}"));
  }

  @Test
  void testCreaHeladeraEnRepositorio() throws RepositoryException {
    Heladera heladeraNueva = CuidadoHeladeraController.tomarCuidadoHeladera(
        "{" +
            "\"nombreHeladera\": \"Heladera\", " +
            "\"capacidadEnViandas\": 1, " +
            "\"idColaboradorSerializado\": \"" + colaborador.getId() + "\", " +
            "\"latitud\": -34.0, " +
            "\"longitud\": -58.0}");

    assertTrue(new HeladerasRepository().findById(heladeraNueva.getId()).isPresent());
  }

  @Test
  void testCreaSuscripcionParaCuidador() throws RepositoryException {
    Heladera heladera = CuidadoHeladeraController.tomarCuidadoHeladera(
        "{" +
            "\"nombreHeladera\": \"Heladera\", " +
            "\"capacidadEnViandas\": 1, " +
            "\"idColaboradorSerializado\": \"" + colaborador.getId() + "\", " +
            "\"latitud\": -34.0, " +
            "\"longitud\": -58.0}");

    assertTrue(SuscripcionRepository
        .getInstancia()
        .get(heladera, MotivoDeDistribucion.FALLA_HELADERA, colaborador)
        .isPresent());
  }
}