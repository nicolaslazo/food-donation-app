package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.DonacionViandas;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.repositories.HibernatePersistenceReset;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.RollbackException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DonacionViandasRepositoryTest {
  CoordenadasGeograficas obelisco = new CoordenadasGeograficas(-34.5611745, -58.4287506);
  Colaborador colaborador = new Colaborador(
          new Documento(TipoDocumento.DNI, 1),
          "",
          "",
          LocalDate.now(),
          new CoordenadasGeograficas(-30d, -50d));

  Vianda vianda = new Vianda("",
          ZonedDateTime.now().plusWeeks(1),
          ZonedDateTime.now(),
          colaborador,
          1d,
          1);

  Heladera  heladera = new Heladera("Una heladera",
          obelisco,
          colaborador,
          50,
          ZonedDateTime.now().minusMonths(5)
  );
  DonacionViandas donacion =
          new DonacionViandas(colaborador, Collections.singletonList(vianda), heladera);

  @BeforeEach
  void setUp() {
    new ColaboradorRepository().insert(colaborador);
    new HeladerasRepository().insert(heladera);
    new DonacionViandasRepository().insert(donacion);
  }

  @AfterEach
  void tearDown() {
    new HibernatePersistenceReset().execute();
  }

  @Test
  void testObtenerPorId() {
    new DonacionViandasRepository().insert(donacion);
    Optional<DonacionViandas> encontrada = new DonacionViandasRepository().findById(donacion.getId());

    assertTrue(encontrada.isPresent());
    assertEquals(donacion.getId(), encontrada.get().getId());
  }

  @Test
  void testObtenerTotalPorColaborador() throws RepositoryException {
    DonacionViandas otraDonacion = new DonacionViandas(colaborador,
        Arrays.asList(mock(Vianda.class), mock(Vianda.class)),
        heladera);

    new DonacionViandasRepository().insert(otraDonacion);

    int total = new DonacionViandasRepository().getTotal(colaborador);

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
        donacion.setFechaRealizada(ZonedDateTime.now());
        new DonacionViandasRepository().insert(donacion);
    });

    Map<Colaborador, Long> cantidades = new DonacionViandasRepository().getCantidadDonacionesPorColaboradorSemanaAnterior();

    assertEquals(1, cantidades.get(unColaboradorMock));
    assertEquals(2, cantidades.get(otroColaboradorMock));
  }

  @Test
  void testInsertarDonacionConViandasRepetidasLanzaExcepcion()   {

    assertThrows(RollbackException.class, () -> new DonacionViandasRepository().insert(donacion));
  }

  @Test
  void testEliminarTodo() {
    new DonacionViandasRepository().deleteAll();
    Stream<DonacionViandas> vacio = new DonacionViandasRepository().findAll();

    assertEquals(0, vacio.count());
  }
}