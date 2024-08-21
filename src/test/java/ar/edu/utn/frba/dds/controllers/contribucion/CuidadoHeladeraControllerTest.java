package ar.edu.utn.frba.dds.controllers.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Email;
import ar.edu.utn.frba.dds.models.entities.contribucion.MotivoDeDistribucion;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
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
import static org.mockito.Mockito.mock;

class CuidadoHeladeraControllerTest {
  Colaborador colaborador = new Colaborador(mock(Email.class),
      new Documento(TipoDocumento.DNI, 1),
      "",
      "",
      LocalDate.now(),
      mock(Ubicacion.class));

  @BeforeEach
  void setUp() throws RepositoryException {
    ColaboradorRepository.getInstancia().insert(colaborador);
  }

  @AfterEach
  void tearDown() {
    ColaboradorRepository.getInstancia().deleteTodos();
    HeladerasRepository.getInstancia().deleteTodas();
    SuscripcionRepository.getInstancia().deleteTodas();
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
    CuidadoHeladeraController.tomarCuidadoHeladera(
        "{" +
            "\"nombreHeladera\": \"Heladera\", " +
            "\"capacidadEnViandas\": 1, " +
            "\"idColaboradorSerializado\": \"" + colaborador.getId() + "\", " +
            "\"latitud\": -34.0, " +
            "\"longitud\": -58.0}");

    assertTrue(HeladerasRepository.getInstancia().get(1).isPresent());
  }

  @Test
  void testCreaSuscripcionParaCuidador() throws RepositoryException {
    CuidadoHeladeraController.tomarCuidadoHeladera(
        "{" +
            "\"nombreHeladera\": \"Heladera\", " +
            "\"capacidadEnViandas\": 1, " +
            "\"idColaboradorSerializado\": \"" + colaborador.getId() + "\", " +
            "\"latitud\": -34.0, " +
            "\"longitud\": -58.0}");

    Heladera heladeraCreada = HeladerasRepository.getInstancia().get(1).get();
    assertTrue(SuscripcionRepository
        .getInstancia()
        .get(heladeraCreada, MotivoDeDistribucion.FALLA_HELADERA, colaborador)
        .isPresent());
  }
}