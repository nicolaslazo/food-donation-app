package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.OfertaRecompensa;
import ar.edu.utn.frba.dds.models.entities.contribucion.RubroRecompensa;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.recompensas.Recompensa;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.repositories.HibernatePersistenceReset;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.recompensas.RecompensasRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class OfertaRecompensaRepositoryTest {
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

  OfertaRecompensa ofertaRecompensa = new OfertaRecompensa(colaborador, recompensa);

  @BeforeEach
  void setUp() {
    new ColaboradorRepository().insert(colaborador);
    new RecompensasRepository().insert(recompensa);
  }

  @AfterEach
  void tearDown() {
    new HibernatePersistenceReset().execute();
  }

  @Test
  void repositorioSeInstancia() {
    assertInstanceOf(OfertaRecompensaRepository.class, new OfertaRecompensaRepository());
  }

  @Test
  void insertaOfertaRecompensasSinFallar() {
    assertDoesNotThrow(() -> new OfertaRecompensaRepository().insert(ofertaRecompensa));
  }

  @Test
  void getRetornaOfertaRecompensaPorId() {
    OfertaRecompensaRepository repositorio = new OfertaRecompensaRepository();

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
    new RecompensasRepository().insertAll(List.of(otraRecompensa, otraRecompensaMas));

    OfertaRecompensa ofertaRecompensa1 = new OfertaRecompensa(colaborador, otraRecompensa);
    OfertaRecompensa ofertaRecompensa2 = new OfertaRecompensa(colaborador, otraRecompensaMas);

    repositorio.insert(ofertaRecompensa);
    repositorio.insert(ofertaRecompensa1);
    repositorio.insert(ofertaRecompensa2);

    // Utilizamos el ID de ofertaRecompensa1 en lugar del ID de otraRecompensa
    assertEquals(ofertaRecompensa1, repositorio.findById(ofertaRecompensa1.getId()).orElseThrow());
  }


  @Test
  void getTodosRetornaTodosLosContenidos() {
    OfertaRecompensaRepository repositorio = new OfertaRecompensaRepository();

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
    new RecompensasRepository().insertAll(List.of(otraRecompensa, otraRecompensaMas));

    OfertaRecompensa ofertaRecompensa1 = new OfertaRecompensa(colaborador, otraRecompensa);
    OfertaRecompensa ofertaRecompensa2 = new OfertaRecompensa(colaborador, otraRecompensaMas);

    repositorio.insert(ofertaRecompensa);
    repositorio.insert(ofertaRecompensa1);
    repositorio.insert(ofertaRecompensa2);

    assertEquals(3, repositorio.findAll().count());
  }
}

