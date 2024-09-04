package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.EntregaTarjetas;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntregaTarjetasRepositoryTest {
  final EntregaTarjetasRepository repositorio = EntregaTarjetasRepository.getInstancia();
  Colaborador colaboradorMock = Mockito.mock(Colaborador.class);
  final EntregaTarjetas entrega =
      new EntregaTarjetas(colaboradorMock, Collections.singletonList(Mockito.mock(Tarjeta.class)));

  @AfterEach
  void tearDown() {
    repositorio.deleteTodo();
  }

  @Test
  void testGetPorId() throws RepositoryException {
    repositorio.insert(entrega);
    Optional<EntregaTarjetas> encontrada = repositorio.get(1L);

    assertTrue(encontrada.isPresent());
    assertEquals(1, encontrada.get().getId());
  }

  @Test
  void testObtenerTotalPorColaborador() throws RepositoryException {
    EntregaTarjetas otraEntrega =
        new EntregaTarjetas(colaboradorMock, Arrays.asList(Mockito.mock(Tarjeta.class), Mockito.mock(Tarjeta.class)));

    repositorio.insert(entrega);
    repositorio.insert(otraEntrega);

    int total = repositorio.getTotal(colaboradorMock);

    assertEquals(1 + 2, total);
  }

  @Test
  void testInsertarEntrega() throws RepositoryException {
    Long id = repositorio.insert(entrega);

    assertEquals(1L, id);
    assertEquals(1L, entrega.getId());
  }

  @Test
  void testInsertarEntregaConTarjetasRepetidasLanzaExcepcion() throws RepositoryException {
    repositorio.insert(entrega);

    assertThrows(RepositoryException.class, () -> repositorio.insert(entrega));
  }

  @Test
  void testEliminarTodo() throws RepositoryException {
    repositorio.insert(entrega);
    repositorio.deleteTodo();

    assertTrue(repositorio.get(1L).isEmpty());
  }
}