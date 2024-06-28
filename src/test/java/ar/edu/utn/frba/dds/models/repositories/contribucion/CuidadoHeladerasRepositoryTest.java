package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.CuidadoHeladera;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.RepositoryInsertException;
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
  void testGetPorId() throws RepositoryInsertException {
    repositorio.insert(contribucion);
    Optional<CuidadoHeladera> encontrada = repositorio.get(1);

    assertTrue(encontrada.isPresent());
    assertEquals(1, encontrada.get().getId());
  }

  @Test
  void testGetMesesActivosCumulativosPorColaborador() throws RepositoryInsertException {
    final Heladera heladera = Mockito.mock(Heladera.class);
    final Heladera otraHeladera = Mockito.mock(Heladera.class);

    CuidadoHeladera contribucion = new CuidadoHeladera(colaboradorMock, heladera);
    CuidadoHeladera otraContribucion = new CuidadoHeladera(colaboradorMock, otraHeladera);

    repositorio.insert(contribucion);
    repositorio.insert(otraContribucion);

    when(heladera.mesesActiva()).thenReturn(3);
    when(otraHeladera.mesesActiva()).thenReturn(5);

    int mesesCumulativos = repositorio.getMesesActivosCumulativos(colaboradorMock);

    assertEquals(3 + 5, mesesCumulativos);
  }

  @Test
  void testInsertContribucion() throws RepositoryInsertException {
    int id = repositorio.insert(contribucion);

    assertEquals(1, id);
    assertEquals(1, contribucion.getId());
  }

  @Test
  void testInsertarContribucionConHeladeraRepetidaLanzaExcepcion() throws RepositoryInsertException {
    repositorio.insert(contribucion);

    assertThrows(RepositoryInsertException.class, () -> repositorio.insert(contribucion));
  }

  @Test
  void testEliminarTodas() throws RepositoryInsertException {
    repositorio.insert(contribucion);
    repositorio.deleteTodas();

    assertTrue(repositorio.get(1).isEmpty());
  }
}
