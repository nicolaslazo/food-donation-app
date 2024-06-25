package ar.edu.utn.frba.dds.models.repositories.heladera;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.models.repositories.RepositoryInsertException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HeladerasRepositoryTest {
  final Ubicacion obelisco = new Ubicacion(-34.5611745, -58.4287506);
  final Colaborador colaboradorMock = Mockito.mock(Colaborador.class);
  final Heladera heladera = new Heladera("Heladera",
      obelisco,
      colaboradorMock,
      50,
      ZonedDateTime.now().minusMonths(5),
      true
  );
  final Heladera otraHeladera = new Heladera("Otra heladera",
      new Ubicacion(-34.0, -58.0),
      colaboradorMock,
      60,
      ZonedDateTime.now().minusMonths(7),
      true
      );
  HeladerasRepository repository;

  @BeforeEach
  void setUp() throws RepositoryInsertException {
    repository = new HeladerasRepository();

    repository.insert(heladera);
  }

  @Test
  void testGetPorId() {
    Optional<Heladera> encontrada = repository.get(1);
    assertTrue(encontrada.isPresent());
    assertEquals(1, encontrada.get().getId());
  }

  @Test
  void testGetPorUbicacion() {
    Optional<Heladera> found = repository.get(obelisco);

    assertTrue(found.isPresent());
    assertEquals(obelisco, found.get().getUbicacion());
  }

  @Test
  void testInsert() {
    assertEquals(1, heladera.getId());
  }

  @Test
  void testFallaCuandoYaExisteHeladeraConEsaUbicacion() {
    assertThrows(RepositoryInsertException.class, () -> repository.insert(heladera));
  }

  @Test
  void testGetTodasPorColaborador() throws RepositoryInsertException {
    repository.insert(otraHeladera);

    List<Heladera> heladerasDelColaborador = repository.getTodas(colaboradorMock);

    assertEquals(2, heladerasDelColaborador.size());
    assertTrue(heladerasDelColaborador.contains(heladera));
    assertTrue(heladerasDelColaborador.contains(otraHeladera));
  }

  @Test
  void testGetMesesActivosCumulativos() throws RepositoryInsertException {
    repository.insert(otraHeladera);

    // Las heladeras necesitan tener una temperatura registrada recientemente para contar para los meses activos
    heladera.setUltimaTempRegistradaCelsius(6);
    otraHeladera.setUltimaTempRegistradaCelsius(6);


    int mesesActivos = repository.getMesesActivosCumulativos(colaboradorMock);

    assertEquals(5 + 7, mesesActivos);
  }
}
