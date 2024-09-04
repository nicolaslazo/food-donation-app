package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.Dinero;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class DineroRepositoryTest {
  final DineroRepository repositorio = DineroRepository.getInstancia();
  final Colaborador colaboradorMock = Mockito.mock(Colaborador.class);
  final Dinero donacion = new Dinero(colaboradorMock, 1000, null);

  @BeforeEach
  void setUp() {
    when(colaboradorMock.getId()).thenReturn(UUID.randomUUID());

    repositorio.deleteTodo();
  }

  @Test
  void testGetPorId() {
    repositorio.insert(donacion);
    Optional<Dinero> encontrada = repositorio.get(1);

    assertTrue(encontrada.isPresent());
    assertEquals(1, encontrada.get().getId());
  }

  @Test
  void testObtenerTotalPorColaborador() {
    Dinero otraDonacion = new Dinero(colaboradorMock, 500, null);
    repositorio.insert(donacion);
    repositorio.insert(otraDonacion);

    double total = repositorio.getTotal(colaboradorMock);

    assertEquals(1500.0, total);
  }

  @Test
  void testInsertarDonacion() {
    Long id = repositorio.insert(donacion);

    assertEquals(1L, id);
    assertEquals(1L, donacion.getId());
  }
}
