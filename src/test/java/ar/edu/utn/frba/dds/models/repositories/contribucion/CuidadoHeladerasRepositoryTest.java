package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.CuidadoHeladera;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class CuidadoHeladerasRepositoryTest {
  final CuidadoHeladerasRepository repositorio = CuidadoHeladerasRepository.getInstancia();
  final Colaborador colaboradorMock = Mockito.mock(Colaborador.class);
  final CuidadoHeladera contribucion = new CuidadoHeladera(colaboradorMock, Mockito.mock(Heladera.class));

  @BeforeEach
  void setUp() {
    repositorio.deleteTodas();
  }

  @Test
  void testGetPorId() throws RepositoryException {
    repositorio.insert(contribucion);
    Optional<CuidadoHeladera> encontrada = repositorio.get(1L);

    assertTrue(encontrada.isPresent());
    assertEquals(1L, encontrada.get().getId());
  }

  @Test
  void testInsertContribucion() throws RepositoryException {
    Long id = repositorio.insert(contribucion);

    assertEquals(1L, id);
    assertEquals(1L, contribucion.getId());
  }

  @Test
  void testInsertarContribucionConHeladeraRepetidaLanzaExcepcion() throws RepositoryException {
    repositorio.insert(contribucion);
    assertThrows(RepositoryException.class, () -> repositorio.insert(contribucion));
  }

  @Test
  void testEliminarTodas() throws RepositoryException {
    repositorio.insert(contribucion);
    repositorio.deleteTodas();

    assertTrue(repositorio.get(1L).isEmpty());
  }
}
