package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.CuidadoHeladera;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.repositories.HibernatePersistenceReset;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.RollbackException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class CuidadoHeladerasRepositoryTest {
  CoordenadasGeograficas obelisco = new CoordenadasGeograficas(-34.5611745, -58.4287506);

  Colaborador colaborador = new Colaborador(
      new Documento(TipoDocumento.DNI, 1),
      "",
      "",
      LocalDate.now(),
      new CoordenadasGeograficas(-30d, -50d));

  Heladera heladera = new Heladera("Una heladera",
      obelisco,
      colaborador,
      50,
      ZonedDateTime.now().minusMonths(5),
      ""
  );

  CuidadoHeladera contribucion = new CuidadoHeladera(colaborador, heladera);

  @BeforeEach
  void setUp() {
    new ColaboradorRepository().insert(colaborador);
    new HeladerasRepository().insert(heladera);
    new CuidadoHeladerasRepository().insert(contribucion);
  }

  @AfterEach
  void tearDown() {
    new HibernatePersistenceReset().execute();
  }

  @Test
  void testGetPorId() {
    Optional<CuidadoHeladera> encontrada = new CuidadoHeladerasRepository().findById(contribucion.getId());

    assertTrue(encontrada.isPresent());
    assertEquals(contribucion.getId(), encontrada.get().getId());
  }


  @Test
  void testInsertarContribucionConHeladeraRepetidaLanzaExcepcion() {
    CuidadoHeladera contribucionDuplicada = new CuidadoHeladera(colaborador, heladera);
    assertThrows(RollbackException.class, () ->
        new CuidadoHeladerasRepository().insert(contribucionDuplicada)
    );
  }

  @Test
  void testEliminarTodas() {
    new CuidadoHeladerasRepository().deleteAll();

    Stream<CuidadoHeladera> contribuciones = new CuidadoHeladerasRepository().findAll();

    assertEquals(0, contribuciones.count());
  }

  @Test
  void testMesesActivos() {
    int mesesActiva = new CuidadoHeladerasRepository().getMesesActivosCumulativos(colaborador);
    assertEquals(5, mesesActiva);
  }
}
