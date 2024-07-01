package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.contribucion.RubroRecompensa;
import ar.edu.utn.frba.dds.models.entities.recompensas.Recompensa;
import ar.edu.utn.frba.dds.models.repositories.recompensas.RecompensasRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class RecompensasRepositoryTest {
  RecompensasRepository repositorio;
  Recompensa recompensaDummy;

  @BeforeEach
  void setUp() {
    repositorio = new RecompensasRepository();
    recompensaDummy = new Recompensa(
        "Recompensa dummy", RubroRecompensa.ELECTRONICA, 100, 1, null
    );
  }

  @Test
  void repositorioSeInstancia() {
    assertInstanceOf(RecompensasRepository.class, repositorio);
  }

  @Test
  void insertaRecompensasSinFallar() {
    assertDoesNotThrow(() -> repositorio.insert(recompensaDummy));
  }

  @Test
  void insertSeteaIdEnRecompensa() {
    assertNotEquals(1, recompensaDummy.getId());

    repositorio.insert(recompensaDummy);

    assertEquals(1, recompensaDummy.getId());
  }

  @Test
  void getRetornaRecompensaPorId() {
    Recompensa otraRecompensa = new Recompensa(
        "Recompensa dummy", RubroRecompensa.ELECTRONICA, 100, 1, null
    );
    Recompensa otraRecompensaMas = new Recompensa(
        "Recompensa dummy", RubroRecompensa.ELECTRONICA, 100, 1, null
    );

    repositorio.insert(recompensaDummy);
    repositorio.insert(otraRecompensa);
    repositorio.insert(otraRecompensaMas);

    assertEquals(otraRecompensa, repositorio.get(2).orElse(null));
  }

  @Test
  void getTodosRetornaTodosLosContenidos() {
    Recompensa otraRecompensa = new Recompensa(
        "Recompensa dummy", RubroRecompensa.ELECTRONICA, 100, 1, null
    );
    Recompensa otraRecompensaMas = new Recompensa(
        "Recompensa dummy", RubroRecompensa.ELECTRONICA, 100, 1, null
    );

    repositorio.insert(recompensaDummy);
    repositorio.insert(otraRecompensa);
    repositorio.insert(otraRecompensaMas);

    assertEquals(3, repositorio.getTodos().size());
  }
}