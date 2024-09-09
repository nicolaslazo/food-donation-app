package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.DonacionViandas;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.HibernatePersistenceReset;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.users.UsuariosRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DonacionViandasRepositoryTest {
  CoordenadasGeograficas obelisco = new CoordenadasGeograficas(-34.5611745, -58.4287506);
  Colaborador colaborador = new Colaborador(
      new Documento(TipoDocumento.DNI, 1),
      "",
      "",
      LocalDate.now(),
      new CoordenadasGeograficas(-30d, -50d));

  Usuario usuario = new Usuario(
      new Documento(TipoDocumento.DNI, 1),
      "",
      "",
      LocalDate.now(),
      new HashSet<>());

  Vianda vianda = new Vianda("Uno",
      ZonedDateTime.now().plusWeeks(1),
      ZonedDateTime.now(),
      colaborador,
      1d,
      1);

  Vianda viandaDos = new Vianda("Dos",
      ZonedDateTime.now().plusWeeks(1),
      ZonedDateTime.now(),
      colaborador,
      1d,
      2);

  Vianda viandaTres = new Vianda("Tres",
      ZonedDateTime.now().plusWeeks(1),
      ZonedDateTime.now(),
      colaborador,
      1d,
      3
  );

  Heladera heladera = new Heladera("Una heladera",
      obelisco,
      colaborador,
      50,
      ZonedDateTime.now().minusMonths(5),
      ""
  );
  DonacionViandas donacion =
      new DonacionViandas(colaborador, Collections.singletonList(vianda), heladera);

  DonacionViandas donacionDos =
      new DonacionViandas(colaborador, Collections.singletonList(viandaDos), heladera);

  DonacionViandas donacionTres =
      new DonacionViandas(colaborador, Collections.singletonList(viandaTres), heladera);

  @BeforeEach
  void setUp() {
    new ColaboradorRepository().insert(colaborador);
    new UsuariosRepository().insert(usuario);
    new HeladerasRepository().insert(heladera);
    new DonacionViandasRepository().insert(donacion);
  }

  @AfterEach
  void tearDown() {
    new HibernatePersistenceReset().execute();
  }

  @Test
  void testObtenerPorId() {
    Optional<DonacionViandas> encontrada = new DonacionViandasRepository().findById(donacion.getId());

    assertTrue(encontrada.isPresent());
    assertEquals(donacion.getId(), encontrada.get().getId());
  }

  @Test
  void testObtenerTotalPorColaborador() {
    new DonacionViandasRepository().insert(donacionDos);
    new DonacionViandasRepository().insert(donacionTres);

    int total = new DonacionViandasRepository().getTotal(colaborador);

    assertEquals(3, total);
  }

  @Test
  void testCuentaDonacionesPorColaborador() {
    Colaborador colaboradorDos = new Colaborador(
        new Documento(TipoDocumento.DNI, 2),
        "",
        "",
        LocalDate.now(),
        new CoordenadasGeograficas(-20d, -20d));

    List<DonacionViandas> donaciones = List.of(
        new DonacionViandas(colaborador, List.of(viandaTres), heladera),
        new DonacionViandas(colaboradorDos, List.of(viandaDos), heladera)
    );

    donaciones.forEach(donacion -> {
      donacion.setFechaRealizada(ZonedDateTime.now());
      new DonacionViandasRepository().insert(donacion);
    });

    Map<Colaborador, Long> cantidades = new DonacionViandasRepository().getCantidadDonacionesPorColaboradorSemanaAnterior();

    assertEquals(1, cantidades.get(colaborador));
    assertEquals(1, cantidades.get(colaboradorDos));
  }

//  TODO: No logro que funcione
//  @Test
//  void testInsertarDonacionConViandasRepetidasLanzaExcepcion() {
//    new DonacionViandasRepository().insert(donacion);
//    assertThrows(RollbackException.class, () ->
//            new DonacionViandasRepository().insert(donacion));
//  }

  @Test
  void testEliminarTodo() {
    new DonacionViandasRepository().deleteAll();
    Stream<DonacionViandas> vacio = new DonacionViandasRepository().findAll();

    assertEquals(0, vacio.count());
  }
}