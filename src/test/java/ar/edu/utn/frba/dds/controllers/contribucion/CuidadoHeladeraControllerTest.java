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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CuidadoHeladeraControllerTest {
  Colaborador colaborador = new Colaborador(
      new Documento(TipoDocumento.DNI, 1),
      "",
      "",
      LocalDate.now(),
      new CoordenadasGeograficas(-34d, -58d));
  static HeladerasRepository heladerasRepository  = new HeladerasRepository();

  @BeforeEach
  void setUp() {
    new ColaboradorRepository().insert(colaborador);
  }

  @AfterEach
  void tearDown() {
    heladerasRepository.deleteAll();
    SuscripcionRepository.getInstancia().deleteTodas();
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
    CuidadoHeladeraController.tomarCuidadoHeladera(
        "{" +
            "\"nombreHeladera\": \"Heladera\", " +
            "\"capacidadEnViandas\": 1, " +
            "\"idColaboradorSerializado\": \"" + colaborador.getId() + "\", " +
            "\"latitud\": -34.0, " +
            "\"longitud\": -58.0}");

    assertTrue(heladerasRepository.findById(1L).isPresent());
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

    Optional<Heladera> heladeraOpcional = heladerasRepository.findById(1L);
    Heladera heladera;
    if(heladeraOpcional.isPresent()) {
      heladera = heladeraOpcional.get();
    } else {
      throw new NoSuchElementException();
    }

    assertTrue(SuscripcionRepository
        .getInstancia()
        .get(heladera, MotivoDeDistribucion.FALLA_HELADERA, colaborador)
        .isPresent());
  }
}