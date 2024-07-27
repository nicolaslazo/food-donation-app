package ar.edu.utn.frba.dds.models.repositories.heladera;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
import org.junit.jupiter.api.AfterEach;
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
  final Heladera heladera = new Heladera("Una heladera",
      obelisco,
      colaboradorMock,
      50,
      ZonedDateTime.now().minusMonths(5)
  );
  final Heladera otraHeladera = new Heladera("Otra heladera",
      new Ubicacion(-34, -58),
      colaboradorMock,
      60,
      ZonedDateTime.now().minusMonths(7)
  );
  HeladerasRepository repository = HeladerasRepository.getInstancia();

  @BeforeEach
  void setUp() throws RepositoryException {
    repository.insert(heladera);
  }

  @AfterEach
  void tearDown() {
    repository.deleteTodas();
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
    assertThrows(RepositoryException.class, () -> repository.insert(heladera));
  }

  @Test
  void testGetTodasPorColaborador() throws RepositoryException {
    repository.insert(otraHeladera);

    List<Heladera> heladerasDelColaborador = repository.getTodas(colaboradorMock);

    assertEquals(2, heladerasDelColaborador.size());
    assertTrue(heladerasDelColaborador.contains(heladera));
    assertTrue(heladerasDelColaborador.contains(otraHeladera));
  }
}
