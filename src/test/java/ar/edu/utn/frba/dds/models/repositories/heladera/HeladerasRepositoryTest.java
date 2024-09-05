package ar.edu.utn.frba.dds.models.repositories.heladera;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.users.UsuariosRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HeladerasRepositoryTest {
  CoordenadasGeograficas obelisco = new CoordenadasGeograficas(-34.5611745, -58.4287506);

  Colaborador colaborador = new Colaborador(
      new Documento(TipoDocumento.DNI, 1),
      "",
      "",
      LocalDate.now(),
      new CoordenadasGeograficas(-30d, -50d));

  Heladera heladera;

  Heladera otraHeladera = new Heladera("Otra heladera",
      new CoordenadasGeograficas(-34d, -58d),
      colaborador,
      60,
      ZonedDateTime.now().minusMonths(7)
  );

  @BeforeEach
  void setUp() {
    heladera = new Heladera("Una heladera",
        obelisco,
        colaborador,
        50,
        ZonedDateTime.now().minusMonths(5)
    );

    new HeladerasRepository().insert(heladera);
  }

  @AfterEach
  void tearDown() {
    new HeladerasRepository().deleteAll();
    new ColaboradorRepository().deleteAll();
    new UsuariosRepository().deleteAll();
  }

  @Test
  void testGetPorId() {
    Optional<Heladera> encontrada = new HeladerasRepository().findById(heladera.getId());
    assertTrue(encontrada.isPresent());
    assertEquals(heladera.getId(), encontrada.get().getId());
  }

// TODO: Borrar si nunca buscamos por ubicación
//  @Test
//  void testGetPorUbicacion() {
//    Optional<Heladera> found = new HeladerasRepository().find(obelisco);
//
//    assertTrue(found.isPresent());
//    assertEquals(obelisco, found.get().getUbicacion());
//  }

  @Test
  void testInsert() {
    Optional<Heladera> encontrada = new HeladerasRepository().findById(heladera.getId());
    assertTrue(encontrada.isPresent());
    assertEquals(heladera.getId(), encontrada.get().getId());
  }

  @Test
  void testGetTodasPorColaborador() {
    new HeladerasRepository().insert(otraHeladera);

    List<Heladera> heladerasDelColaborador = new HeladerasRepository().findAll(colaborador).toList();
    assertEquals(2, heladerasDelColaborador.size());
    assertTrue(heladerasDelColaborador.contains(heladera));
    assertTrue(heladerasDelColaborador.contains(otraHeladera));
  }

//  @Test
//  void testFallaCuandoYaExisteHeladeraConEsaUbicacion() {
//    Heladera heladeraCopiona = new Heladera(
//        "Heladera Copiona",
//        obelisco,
//        colaborador,
//        60,
//        ZonedDateTime.now().minusMonths(7)
//    );
//
//    assertThrows(RollbackException.class, () -> new HeladerasRepository().insert(heladeraCopiona));
//  }
}
