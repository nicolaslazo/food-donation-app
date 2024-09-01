package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.DonacionViandas;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DonacionViandasRepositoryTest {
  final DonacionViandasRepository repositorio = DonacionViandasRepository.getInstancia();
  final Colaborador colaboradorMock = mock(Colaborador.class);
  final Vianda viandaMock = mock(Vianda.class);
  final Heladera heladeraMock = mock(Heladera.class);
  final DonacionViandas donacion = new DonacionViandas(colaboradorMock, Collections.singletonList(viandaMock), heladeraMock);

  @BeforeEach
  void setUp() {
    when(colaboradorMock.getId()).thenReturn(UUID.randomUUID());
  }

  @AfterEach
  void tearDown() {
    repositorio.deleteTodas();
  }

  @Test
  void testObtenerPorId() throws RepositoryException {
    repositorio.insert(donacion);
    Optional<DonacionViandas> encontrada = repositorio.get(1);

    assertTrue(encontrada.isPresent());
    assertEquals(1, encontrada.get().getId());
  }

  @Test
  void testObtenerTotalPorColaborador() throws RepositoryException {
    DonacionViandas otraDonacion = new DonacionViandas(colaboradorMock,
        Arrays.asList(mock(Vianda.class), mock(Vianda.class)),
        heladeraMock);
    repositorio.insert(donacion);
    repositorio.insert(otraDonacion);

    int total = repositorio.getTotal(colaboradorMock);

    assertEquals(3, total);
  }

  @Test
  void testCuentaDonacionesPorColaborador() {
    Colaborador unColaboradorMock = mock(Colaborador.class);
    Colaborador otroColaboradorMock = mock(Colaborador.class);

    List<DonacionViandas> donaciones = List.of(
        new DonacionViandas(unColaboradorMock, List.of(mock(Vianda.class)), mock(Heladera.class)),
        new DonacionViandas(otroColaboradorMock, List.of(mock(Vianda.class)), mock(Heladera.class)),
        new DonacionViandas(otroColaboradorMock, List.of(mock(Vianda.class)), mock(Heladera.class))
    );

    donaciones.forEach(donacion -> {
      try {
        donacion.setFechaRealizada(ZonedDateTime.now());

        repositorio.insert(donacion);
      } catch (RepositoryException e) {
        throw new RuntimeException(e);
      }
    });

    Map<Colaborador, Integer> cantidades = repositorio.getCantidadDonacionesPorColaboradorSemanaAnterior();

    assertEquals(1, cantidades.get(unColaboradorMock));
    assertEquals(2, cantidades.get(otroColaboradorMock));
  }

  @Test
  void testInsertarDonacion() throws RepositoryException {
    int id = repositorio.insert(donacion);

    assertEquals(1, id);
    assertEquals(1, donacion.getId());
  }

  @Test
  void testInsertarDonacionConViandasRepetidasLanzaExcepcion() throws RepositoryException {
    repositorio.insert(donacion);

    assertThrows(RepositoryException.class, () -> repositorio.insert(donacion));
  }

  @Test
  void testEliminarTodo() throws RepositoryException {
    repositorio.insert(donacion);

    repositorio.deleteTodas();

    assertTrue(repositorio.get(1).isEmpty());
  }
}