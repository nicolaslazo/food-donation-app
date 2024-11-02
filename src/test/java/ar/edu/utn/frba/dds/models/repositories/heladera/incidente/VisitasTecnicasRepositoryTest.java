package ar.edu.utn.frba.dds.models.repositories.heladera.incidente;

import ar.edu.utn.frba.dds.models.entities.Tecnico;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.Incidente;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.TipoIncidente;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.VisitaTecnica;
import ar.edu.utn.frba.dds.models.entities.ubicacion.AreaGeografica;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.repositories.HibernatePersistenceReset;
import ar.edu.utn.frba.dds.models.repositories.TecnicoRepository;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.users.RolesRepository;
import ar.edu.utn.frba.dds.services.seeders.SeederRoles;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.RollbackException;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class VisitasTecnicasRepositoryTest {
  final VisitasTecnicasRepository repositorio = new VisitasTecnicasRepository();
  final IncidenteRepository repositorioIncidentes = new IncidenteRepository();
  Incidente incidente;
  CoordenadasGeograficas obelisco = new CoordenadasGeograficas(-34.5611745, -58.4287506);
  Colaborador colaborador;
  Heladera heladera;
  Incidente incidente2;

  Tecnico tecnico;

  @BeforeEach
  void setUp() {
    new SeederRoles().seedRoles();

    colaborador = new Colaborador(
        new Documento(TipoDocumento.DNI, 1),
        "",
        "",
        LocalDate.now(),
        new CoordenadasGeograficas(-30d, -50d));
    new ColaboradorRepository().insert(colaborador);

    tecnico = new Tecnico(
        new Documento(TipoDocumento.DNI, 1),
        " ",
        " ", LocalDate.now(),
        " ", new AreaGeografica(obelisco, 1),
        null,
        new RolesRepository().findByName("TECNICO").get()
    );

    heladera = new Heladera("Una heladera",
        obelisco,
        colaborador,
        50,
        ZonedDateTime.now().minusMonths(5),
        ""
    );

    incidente = new Incidente(heladera, TipoIncidente.FRAUDE, ZonedDateTime.now());
    incidente2 = new Incidente(heladera, TipoIncidente.ALTA_TEMPERATURA, ZonedDateTime.now());

    new TecnicoRepository().insert(tecnico);
    repositorioIncidentes.insert(incidente);
    repositorioIncidentes.insert(incidente2);
  }

  @AfterEach
  void tearDown() {
    new HibernatePersistenceReset().execute();
  }

  @Test
  void testGetPorId() {
    VisitaTecnica visitaTecnica = new VisitaTecnica(tecnico, incidente, ZonedDateTime.now(), false);

    repositorio.insert(visitaTecnica);

    assertEquals(visitaTecnica, repositorio.findById(visitaTecnica.getId()).orElseThrow());
  }

  @Test
  void testInsertarRedistribucionSinFallar() {
    assertDoesNotThrow(() -> new VisitasTecnicasRepository().insert(new VisitaTecnica(tecnico, incidente, ZonedDateTime.now(), false)));
  }

  @Test
  void testEliminarTodas() {
    repositorio.insert(new VisitaTecnica(tecnico, incidente, ZonedDateTime.now(), false));
    repositorio.deleteAll();
    assertEquals(0, repositorio.findAll().count());
  }

  @Test
  void getTodosRetornaTodosLosContenidos() {

    repositorio.insert(new VisitaTecnica(tecnico, incidente, ZonedDateTime.now(), false));
    repositorio.insert(new VisitaTecnica(tecnico, incidente2, ZonedDateTime.now(), false));

    assertEquals(2, repositorio.findAll().count());
  }

  @Test
  void testVisitaSinResolucionNoResuelveIncidente() {
    repositorio.insert(new VisitaTecnica(tecnico, incidente, ZonedDateTime.now(), false));

    assertFalse(repositorio.isIncidenteResuelto(incidente));
  }

  @Test
  void testVisitaResuelveIncidente() {
    repositorio.insert(new VisitaTecnica(tecnico, incidente, ZonedDateTime.now(), true));

    assertTrue(repositorio.isIncidenteResuelto(incidente));
  }

  @Test
  void insertarVisitaParaIncidenteResueltoFalla() {
    repositorio.insert(new VisitaTecnica(tecnico, incidente, ZonedDateTime.now(), true));

    assertThrows(RollbackException.class,
        () -> repositorio.insert(
            new VisitaTecnica(tecnico, incidente, ZonedDateTime.now(), true))
    );
  }
}