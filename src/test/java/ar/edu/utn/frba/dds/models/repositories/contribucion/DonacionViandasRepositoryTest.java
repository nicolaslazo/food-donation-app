package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.DonacionViandas;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.repositories.RepositoryInsertException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class DonacionViandasRepositoryTest {
  final DonacionViandasRepository repositorio = DonacionViandasRepository.getInstancia();
  final Colaborador colaboradorMock = Mockito.mock(Colaborador.class);
  final Vianda viandaMock = Mockito.mock(Vianda.class);
  final DonacionViandas donacion = new DonacionViandas(colaboradorMock, Collections.singletonList(viandaMock));

  @BeforeEach
  void setUp() {
    when(colaboradorMock.getDocumento()).thenReturn(new Documento(TipoDocumento.DNI, 1));

    repositorio.deleteTodo();
  }

  @Test
  void testObtenerPorId() throws RepositoryInsertException {
    repositorio.insert(donacion);
    Optional<DonacionViandas> encontrada = repositorio.get(1);

    assertTrue(encontrada.isPresent());
    assertEquals(1, encontrada.get().getId());
  }

  @Test
  void testObtenerTotalPorColaborador() throws RepositoryInsertException {
    DonacionViandas otraDonacion = new DonacionViandas(colaboradorMock,
        Arrays.asList(Mockito.mock(Vianda.class), Mockito.mock(Vianda.class)));
    repositorio.insert(donacion);
    repositorio.insert(otraDonacion);

    int total = repositorio.getTotal(colaboradorMock);

    assertEquals(3, total);
  }

  @Test
  void testInsertarDonacion() throws RepositoryInsertException {
    int id = repositorio.insert(donacion);

    assertEquals(1, id);
    assertEquals(1, donacion.getId());
  }

  @Test
  void testInsertarDonacionConViandasRepetidasLanzaExcepcion() throws RepositoryInsertException {
    repositorio.insert(donacion);

    assertThrows(RepositoryInsertException.class, () -> repositorio.insert(donacion));
  }

  @Test
  void testEliminarTodo() throws RepositoryInsertException {
    repositorio.insert(donacion);

    repositorio.deleteTodo();

    assertTrue(repositorio.get(1).isEmpty());
  }
}