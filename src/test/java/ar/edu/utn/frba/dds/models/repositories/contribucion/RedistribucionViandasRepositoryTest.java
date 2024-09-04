package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.RedistribucionViandas;
import org.junit.jupiter.api.AfterEach;
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
      Collections.singletonList(null),
      null,
      null,
      null
  );

  @AfterEach
  void tearDown() {
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
        Arrays.asList(null, null),
        null,
        null,
        null
    );

    repositorio.insert(redistribucion);
    repositorio.insert(otraRedistribucion);

    int total = repositorio.getTotal(colaboradorMock);
    assertEquals(1 + 2, total);
  }

  @Test
  void testInsertarRedistribucion() {
    Long id = repositorio.insert(redistribucion);

    assertEquals(1L, id);
    assertEquals(1L, redistribucion.getId());
  }

  @Test
  void testEliminarTodas() {
    repositorio.insert(redistribucion);

    repositorio.deleteTodas();

    assertTrue(repositorio.get(1).isEmpty());
  }
}
