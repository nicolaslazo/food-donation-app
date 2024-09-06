package ar.edu.utn.frba.dds.models.repositories.heladera.incidente;

import ar.edu.utn.frba.dds.models.entities.Tecnico;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.Incidente;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.TipoIncidente;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.VisitaTecnica;
import ar.edu.utn.frba.dds.models.repositories.HibernatePersistenceReset;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class VisitasTecnicasRepositoryTest {
  final VisitasTecnicasRepository repositorio = new VisitasTecnicasRepository();
  final IncidenteRepository repositorioIncidentes = new IncidenteRepository();
  final Incidente incidente = new Incidente(mock(Heladera.class), mock(TipoIncidente.class), ZonedDateTime.now());

  @BeforeEach
  void setUp() {
    repositorioIncidentes.insert(incidente);
  }

  @AfterEach
  void tearDown() {
    new HibernatePersistenceReset().execute();
  }

  @Test
  void testVisitaSinResolucionNoResuelveIncidente() throws RepositoryException {
    repositorio.insert(new VisitaTecnica(mock(Tecnico.class), incidente, ZonedDateTime.now(), false));

    assertFalse(repositorio.isIncidenteResuelto(incidente));
  }

  @Test
  void testVisitaResuelveIncidente() throws RepositoryException {
    repositorio.insert(new VisitaTecnica(mock(Tecnico.class), incidente, ZonedDateTime.now(), true));

    assertTrue(repositorio.isIncidenteResuelto(incidente));
  }

  @Test
  void insertarVisitaParaIncidenteResueltoFalla() throws RepositoryException {
    repositorio.insert(new VisitaTecnica(mock(Tecnico.class), incidente, ZonedDateTime.now(), true));

    assertThrows(RepositoryException.class,
        () -> repositorio.insert(
            new VisitaTecnica(mock(Tecnico.class), incidente, ZonedDateTime.now(), true)));
  }
}