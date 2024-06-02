package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.Dinero;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class DineroRepositoryTest {
  final DineroRepository repositorio = DineroRepository.getInstancia();
  final Colaborador colaboradorMock = Mockito.mock(Colaborador.class);
  final Dinero donacion = new Dinero(colaboradorMock, ZonedDateTime.now(), 1000, null);

  @BeforeEach
  void setUp() {
    when(colaboradorMock.getDocumento()).thenReturn(new Documento(TipoDocumento.DNI, 1));

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
    Dinero otraDonacion = new Dinero(colaboradorMock, ZonedDateTime.now(), 500, null);
    repositorio.insert(donacion);
    repositorio.insert(otraDonacion);

    double total = repositorio.getTotal(colaboradorMock);

    assertEquals(1500.0, total);
  }

  @Test
  void testInsertarDonacion() {
    int id = repositorio.insert(donacion);

    assertEquals(1, id);
    assertEquals(1, donacion.getId());
  }
}
