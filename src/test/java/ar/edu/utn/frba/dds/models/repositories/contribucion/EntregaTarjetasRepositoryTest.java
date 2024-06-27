package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.EntregaTarjetas;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.repositories.RepositoryInsertException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntregaTarjetasRepositoryTest {
  final EntregaTarjetasRepository repositorio = EntregaTarjetasRepository.getInstancia();
  Colaborador colaboradorMock = Mockito.mock(Colaborador.class);
  final EntregaTarjetas entrega = new EntregaTarjetas(colaboradorMock,
      ZonedDateTime.now(),
      Collections.singletonList(Mockito.mock(Tarjeta.class)));

  @BeforeEach
  void setUp() {
    repositorio.deleteTodo();
  }

  @Test
  void testGetPorId() throws RepositoryInsertException {
    repositorio.insert(entrega);
    Optional<EntregaTarjetas> encontrada = repositorio.get(1);

    assertTrue(encontrada.isPresent());
    assertEquals(1, encontrada.get().getId());
  }

  @Test
  void testObtenerTotalPorColaborador() throws RepositoryInsertException {
    EntregaTarjetas otraEntrega = new EntregaTarjetas(colaboradorMock,
        ZonedDateTime.now(),
        Arrays.asList(Mockito.mock(Tarjeta.class), Mockito.mock(Tarjeta.class)));

    repositorio.insert(entrega);
    repositorio.insert(otraEntrega);

    int total = repositorio.getTotal(colaboradorMock);

    assertEquals(1 + 2, total);
  }

  @Test
  void testInsertarEntrega() throws RepositoryInsertException {
    int id = repositorio.insert(entrega);

    assertEquals(1, id);
    assertEquals(1, entrega.getId());
  }

  @Test
  void testInsertarEntregaConTarjetasRepetidasLanzaExcepcion() throws RepositoryInsertException {
    repositorio.insert(entrega);

    assertThrows(RepositoryInsertException.class, () -> repositorio.insert(entrega));
  }

  @Test
  void testEliminarTodo() throws RepositoryInsertException {
    repositorio.insert(entrega);
    repositorio.deleteTodo();

    assertTrue(repositorio.get(1).isEmpty());
  }
}