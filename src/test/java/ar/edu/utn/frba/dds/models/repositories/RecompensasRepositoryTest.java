package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.RubroRecompensa;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.recompensas.Recompensa;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.repositories.recompensas.RecompensasRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RecompensasRepositoryTest {
  Colaborador colaborador = new Colaborador(new Documento(TipoDocumento.DNI, 1),
      "",
      "",
      LocalDate.now(),
      new CoordenadasGeograficas(-34., -58.));
  Recompensa recompensa = new Recompensa("Recompensa dummy",
      colaborador,
      100L,
      1,
      RubroRecompensa.ELECTRONICA,
      null);

  @AfterEach
  void tearDown() {
    new HibernatePersistenceReset().execute();
  }

  @Test
  void repositorioSeInstancia() {
    assertInstanceOf(RecompensasRepository.class, new RecompensasRepository());
  }

  @Test
  void insertaRecompensasSinFallar() {
    assertDoesNotThrow(() -> new RecompensasRepository().insert(recompensa));
  }

  @Test
  void insertSeteaIdEnRecompensa() {
    new RecompensasRepository().insert(recompensa);

    assertNotNull(recompensa.getId());
  }

  @Test
  void getRetornaRecompensaPorId() {
    RecompensasRepository repositorio = new RecompensasRepository();

    Recompensa otraRecompensa = new Recompensa("Recompensa dummy",
        colaborador,
        100L,
        1,
        RubroRecompensa.ELECTRONICA,
        null);
    Recompensa otraRecompensaMas = new Recompensa("Recompensa dummy",
        colaborador,
        100L,
        1,
        RubroRecompensa.ELECTRONICA,
        null);

    repositorio.insert(recompensa);
    repositorio.insert(otraRecompensa);
    repositorio.insert(otraRecompensaMas);

    assertEquals(otraRecompensa, repositorio.findById(otraRecompensa.getId()).orElseThrow());
  }

  @Test
  void getTodosRetornaTodosLosContenidos() {
    RecompensasRepository repositorio = new RecompensasRepository();

    Recompensa otraRecompensa = new Recompensa("Recompensa dummy",
        colaborador,
        100L,
        1,
        RubroRecompensa.ELECTRONICA,
        null);
    Recompensa otraRecompensaMas = new Recompensa("Recompensa dummy",
        colaborador,
        100L,
        1,
        RubroRecompensa.ELECTRONICA,
        null);

    repositorio.insert(recompensa);
    repositorio.insert(otraRecompensa);
    repositorio.insert(otraRecompensaMas);

    assertEquals(3, repositorio.findAll().count());
  }
}