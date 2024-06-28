package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.DonacionViandas;
import ar.edu.utn.frba.dds.models.repositories.RepositoryInsertException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DonacionViandasRepositoryTest {

  @Mock
  private List<DonacionViandas> donaciones;

  @InjectMocks
  private DonacionViandasRepository repository;

  @Mock
  private Colaborador colaboradorMock;

  @Mock
  private Vianda viandaMock;

  @Test
  public void testObtenerPorId() throws RepositoryInsertException {
    repository.insert(new DonacionViandas(colaboradorMock, ZonedDateTime.now(), Collections.singletonList(viandaMock)));
    Optional<DonacionViandas> encontrada = repository.get(1);

    assertTrue(encontrada.isPresent());
    assertEquals(1, encontrada.get().getId());
  }

  @Test
  public void testObtenerTotalPorColaborador() throws RepositoryInsertException {
    DonacionViandas otraDonacion = new DonacionViandas(colaboradorMock,
        ZonedDateTime.now(),
        Arrays.asList(mock(Vianda.class), mock(Vianda.class)));
    repository.insert(new DonacionViandas(colaboradorMock, ZonedDateTime.now(), Collections.singletonList(viandaMock)));
    repository.insert(otraDonacion);

    int total = repository.getTotal(colaboradorMock);

    assertEquals(3, total);
  }

  @Test
  public void testInsertarDonacion() throws RepositoryInsertException {
    int id = repository.insert(new DonacionViandas(colaboradorMock, ZonedDateTime.now(), Collections.singletonList(viandaMock)));

    assertEquals(1, id);
  }

  @Test
  public void testInsertarDonacionConViandasRepetidasLanzaExcepcion() throws RepositoryInsertException {
    repository.insert(new DonacionViandas(colaboradorMock, ZonedDateTime.now(), Collections.singletonList(viandaMock)));

    assertThrows(RepositoryInsertException.class, () -> repository.insert(new DonacionViandas(colaboradorMock, ZonedDateTime.now(), Collections.singletonList(viandaMock))));
  }

  @Test
  public void testEliminarTodo() throws RepositoryInsertException {
    repository.insert(new DonacionViandas(colaboradorMock, ZonedDateTime.now(), Collections.singletonList(viandaMock)));

    repository.deleteTodo();

    assertTrue(repository.get(1).isEmpty());
  }

  public void setUpTestDonacionSemanaAnterior() throws RepositoryInsertException {
    ZonedDateTime ahora = ZonedDateTime.now();

    when(colaboradorMock.getNombre()).thenReturn("Colaborador Mock");

    List<Vianda> viandas1 = new ArrayList<>();
    viandas1.add(viandaMock);

    DonacionViandas donacion1 = new DonacionViandas(colaboradorMock, ahora.minusDays(2), viandas1); // Fecha hace 2 días

    repository.insert(donacion1);

  }

  @Test
  public void testHayDonacionesSemanaAnterior() throws RepositoryInsertException {
    setUpTestDonacionSemanaAnterior();
    List<DonacionViandas> donacionesSemanaAnterior = repository.obtenerDonacionesSemanaAnterior();

    assertEquals(1, donacionesSemanaAnterior.size());
    assertEquals("Colaborador Mock", donacionesSemanaAnterior.get(0).getColaborador().getNombre());
  }


  public void setUpTestNOHayDonacionSemanaAnterior() throws RepositoryInsertException {
    ZonedDateTime ahora = ZonedDateTime.now();

    List<Vianda> viandas1 = new ArrayList<>();
    viandas1.add(viandaMock);

    DonacionViandas donacion1 = new DonacionViandas(colaboradorMock, ahora.minusDays(10), viandas1); // Fecha hace 2 días

    repository.insert(donacion1);

  }
  @Test
  public void testNOHayDonacionesSemanaAnterior() throws RepositoryInsertException {
    setUpTestNOHayDonacionSemanaAnterior();
    List<DonacionViandas> donacionesSemanaAnterior = repository.obtenerDonacionesSemanaAnterior();

    assertEquals(0, donacionesSemanaAnterior.size());
  }

}
