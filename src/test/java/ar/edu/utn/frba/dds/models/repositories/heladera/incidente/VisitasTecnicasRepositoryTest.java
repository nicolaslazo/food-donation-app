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
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
import ar.edu.utn.frba.dds.models.repositories.TecnicoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.RollbackException;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class VisitasTecnicasRepositoryTest {
  final VisitasTecnicasRepository repositorio = new VisitasTecnicasRepository();
  final IncidenteRepository repositorioIncidentes = new IncidenteRepository();
  final TecnicoRepository tecnicoRepository= new TecnicoRepository();
  CoordenadasGeograficas obelisco = new CoordenadasGeograficas(-34.5611745, -58.4287506);
  Colaborador colaborador = new Colaborador(
      new Documento(TipoDocumento.DNI, 1),
      "",
      "",
      LocalDate.now(),
      new CoordenadasGeograficas(-30d, -50d));
  Heladera heladera = new Heladera("Una heladera",
      obelisco,
      colaborador,
      50,
      ZonedDateTime.now().minusMonths(5)
  );

  final Incidente incidente = new Incidente(heladera, TipoIncidente.FRAUDE, ZonedDateTime.now());

  Tecnico tecnico = new Tecnico(
          new Documento(TipoDocumento.DNI, 1),
          " ",
          " ", LocalDate.now(),
          " ", new AreaGeografica(obelisco, 1)
  );

  @BeforeEach
  void setUp() {
    tecnicoRepository.insert(tecnico);
    repositorioIncidentes.insert(incidente);
  }

  @AfterEach
  void tearDown() {
    new HibernatePersistenceReset().execute();
  }

  @Test
  void testVisitaSinResolucionNoResuelveIncidente() throws RepositoryException {
    repositorio.insert(new VisitaTecnica(tecnico, incidente, ZonedDateTime.now(), false));

    assertFalse(repositorio.isIncidenteResuelto(incidente));
  }

  @Test
  void testVisitaResuelveIncidente() throws RepositoryException {
    repositorio.insert(new VisitaTecnica(tecnico, incidente, ZonedDateTime.now(), true));

    assertTrue(repositorio.isIncidenteResuelto(incidente));
  }

  @Test
  void insertarVisitaParaIncidenteResueltoFalla()  {
    repositorio.insert(new VisitaTecnica(tecnico, incidente, ZonedDateTime.now(), true));

    assertThrows(RollbackException.class,
        () -> repositorio.insert(
            new VisitaTecnica(tecnico, incidente, ZonedDateTime.now(), true))
    );
  }
}