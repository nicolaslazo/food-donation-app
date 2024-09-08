package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.RedistribucionViandas;
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
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

class RedistribucionViandasRepositoryTest {
  final RedistribucionViandasRepository repositorio = new RedistribucionViandasRepository();
  Colaborador colaborador = new Colaborador(new Documento(TipoDocumento.DNI, 1),
      "",
      "",
      LocalDate.now(),
      new CoordenadasGeograficas(-34., -58.));
  CoordenadasGeograficas obelisco = new CoordenadasGeograficas(-34.5611745, -58.4287506);
  Vianda vianda = new Vianda("",
      ZonedDateTime.now().plusWeeks(1),
      ZonedDateTime.now(),
      colaborador,
      1d,
      1);

  Heladera heladera = new Heladera("Una heladera",
      obelisco,
      colaborador,
      50,
      ZonedDateTime.now().minusMonths(5)
  );

  RedistribucionViandas otraRedistribucion = new RedistribucionViandas(colaborador,
      Arrays.asList(null, null),
      heladera,
      heladera,
      null
  );
  final RedistribucionViandas redistribucion = new RedistribucionViandas(colaborador,
      Collections.singletonList(null),
      heladera,
      heladera,
      null
  );

  @AfterEach
  void tearDown() {
    new HibernatePersistenceReset().execute();
  }


  @BeforeEach
  void setUp() {
    new ColaboradorRepository().insert(colaborador);
    new HeladerasRepository().insert(heladera);
  }
  @Test
  void testGetPorId() {

    repositorio.insert(redistribucion);
    repositorio.insert(otraRedistribucion);

    assertEquals(otraRedistribucion, repositorio.findById(otraRedistribucion.getId()).orElseThrow());
  }

  @Test
  void testGetTotalPorColaborador() {
   /* RedistribucionViandas otraRedistribucion = new RedistribucionViandas(colaborador,
        Arrays.asList(null, null),
        null,
        null,
        null
    );

    repositorio.insert(redistribucion);
    repositorio.insert(otraRedistribucion);

    int total = repositorio.getTotal(colaborador);
    assertEquals(1 + 2, total);*/
  }

  @Test
  void testInsertarRedistribucionSinFallar() {
    assertDoesNotThrow(()-> new RedistribucionViandasRepository().insert(redistribucion));
  }

  @Test
  void testEliminarTodas() {
    repositorio.insert(redistribucion);
    repositorio.insert(otraRedistribucion);
    repositorio.deleteAll();
    assertEquals(0, repositorio.findAll().count());
  }

  @Test
  void getTodosRetornaTodosLosContenidos() {

    repositorio.insert(otraRedistribucion);
    repositorio.insert(redistribucion);

    assertEquals(2, repositorio.findAll().count());
  }
}

