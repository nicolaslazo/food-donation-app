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

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ar.edu.utn.frba.dds.models.repositories.contribucion.DonacionViandasRepository.calcularViandasPorColaboradorSemanaAnterior;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DonacionViandasRepositoryTest {
  final DonacionViandasRepository repositorio = DonacionViandasRepository.getInstancia();
  final Colaborador colaboradorMock = mock(Colaborador.class);
  final Vianda viandaMock = mock(Vianda.class);
  final DonacionViandas donacion = new DonacionViandas(colaboradorMock,
      ZonedDateTime.now(),
      Collections.singletonList(viandaMock));

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
        ZonedDateTime.now(),
        Arrays.asList(mock(Vianda.class), mock(Vianda.class)));
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

  // Mock de la lista de donaciones
  private List<DonacionViandas> donaciones;

  @BeforeEach
  public void setUpNuevo() {
    donaciones = new ArrayList<>();

    // Mock de ZonedDateTime.now() para la fecha actual en el método
    ZonedDateTime fechaHoy = ZonedDateTime.now();

    // Mock de ZonedDateTime.minusWeeks(1) para la fecha de la semana anterior
    ZonedDateTime fechaSemanaAnterior = fechaHoy.minusWeeks(1);

    // Creación de objetos mock para Colaborador y Viandas
    Colaborador colaboradorMock1 = mock(Colaborador.class);
    when(colaboradorMock1.getNombre()).thenReturn("Juan");
    when(colaboradorMock1.getApellido()).thenReturn("Perez");

    Colaborador colaboradorMock2 = mock(Colaborador.class);
    when(colaboradorMock2.getNombre()).thenReturn("Maria");
    when(colaboradorMock2.getApellido()).thenReturn("Gomez");

    Vianda viandaMock1 = mock(Vianda.class);
    Vianda viandaMock2 = mock(Vianda.class);

    // Creación de objetos DonacionViandas mock y su fecha
    DonacionViandas donacion1 = new DonacionViandas(colaboradorMock1, fechaHoy.minusDays(2), List.of(viandaMock1));
    DonacionViandas donacion2 = new DonacionViandas(colaboradorMock2, fechaHoy.minusDays(5), List.of(viandaMock1, viandaMock2));
    DonacionViandas donacion3 = new DonacionViandas(colaboradorMock1, fechaHoy.minusWeeks(2), List.of(viandaMock2));

    // Añadir donaciones a la lista mock
    donaciones.add(donacion1);
    donaciones.add(donacion2);
    donaciones.add(donacion3);
  }

}