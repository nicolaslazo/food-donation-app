package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.RedistribucionViandas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RedistribucionViandasRepositoryTest {
  final RedistribucionViandasRepository repositorio = RedistribucionViandasRepository.getInstancia();
  final Colaborador colaboradorMock = Mockito.mock(Colaborador.class);
  final RedistribucionViandas redistribucion = new RedistribucionViandas(colaboradorMock,
      null,
      null,
      null,
      Collections.singletonList(null));

  @BeforeEach
  void setUp() {
    repositorio.deleteTodas();
  }

  @Test
  void testGetPorId() {
    repositorio.insert(redistribucion);
    Optional<RedistribucionViandas> encontrada = repositorio.get(1);

    assertTrue(encontrada.isPresent());
    assertEquals(1, encontrada.get().getId());
  }

  @Test
  void testGetTotalPorColaborador() {
    RedistribucionViandas otraRedistribucion = new RedistribucionViandas(colaboradorMock,
        null,
        null,
        null,
        Arrays.asList(null, null));

    repositorio.insert(redistribucion);
    repositorio.insert(otraRedistribucion);

    int total = repositorio.getTotal(colaboradorMock);
    assertEquals(1 + 2, total);
  }

  @Test
  void testInsertarRedistribucion() {
    int id = repositorio.insert(redistribucion);

    assertEquals(1, id);
    assertEquals(1, redistribucion.getId());
  }

  @Test
  void testEliminarTodas() {
    repositorio.insert(redistribucion);

    repositorio.deleteTodas();

    assertTrue(repositorio.get(1).isEmpty());
  }
}
