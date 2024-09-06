package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ViandasRepositoryTest {
  Colaborador colaborador;
  Heladera heladera;
  Vianda vianda, otraVianda;

  @BeforeEach
  void setUp() {
    colaborador = new Colaborador(new Documento(TipoDocumento.DNI, 1),
        "",
        "",
        LocalDate.now(),
        null);
    heladera = new Heladera("",
        new CoordenadasGeograficas(-34d, -58d),
        colaborador,
        1,
        ZonedDateTime.now());
    vianda = new Vianda("",
        ZonedDateTime.now().plusWeeks(1),
        ZonedDateTime.now(),
        colaborador,
        1d,
        1);
    otraVianda = new Vianda("",
        ZonedDateTime.now().plusWeeks(1),
        ZonedDateTime.now(),
        colaborador,
        1d,
        1);

    new HeladerasRepository().insert(heladera);
  }

  @AfterEach
  void tearDown() {
    new HibernatePersistenceReset().execute();
  }

  @Test
  void testInsertFallaSiHeladeraNoTieneEspacio() {
    vianda.setHeladera(heladera);

    new ViandasRepository().insert(vianda);  // Heladera ahora está llena

    assertThrows(RuntimeException.class, () -> new ViandasRepository().insert(vianda));
  }

  @Test
  void testInsertDeCollectionFallaSiViandasTienenHeladerasDistintas() {
    vianda.setHeladera(heladera);

    assertNull(otraVianda.getHeladera());
    assertThrows(RuntimeException.class,
        () -> new ViandasRepository().insertAll(List.of(otraVianda, vianda)));
  }

  @Test
  void testInsertDeCollectionFallaSiHayDemasiadasViandas() {
    vianda.setHeladera(heladera);
    otraVianda.setHeladera(heladera);

    assertThrows(RuntimeException.class,
        () -> new ViandasRepository().insertAll(List.of(vianda, otraVianda)));
    assertEquals(0, new ViandasRepository().findAll().count());
  }
}