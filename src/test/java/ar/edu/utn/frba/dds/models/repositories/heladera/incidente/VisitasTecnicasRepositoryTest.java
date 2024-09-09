package ar.edu.utn.frba.dds.models.repositories.heladera.incidente;

import ar.edu.utn.frba.dds.models.entities.Tecnico;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.Incidente;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.TipoIncidente;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.VisitaTecnica;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.repositories.HibernatePersistenceReset;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class VisitasTecnicasRepositoryTest {
  VisitasTecnicasRepository repositorio = VisitasTecnicasRepository.getInstancia();
  IncidenteRepository repositorioIncidentes = new IncidenteRepository();
  Colaborador colaborador = new Colaborador(new Documento(TipoDocumento.DNI, 1),
      "",
      "",
      LocalDate.now(),
      new CoordenadasGeograficas(-34d, -58d));
  Heladera heladera = new Heladera("",
      new CoordenadasGeograficas(-34d, -58d),
      colaborador,
      1,
      ZonedDateTime.now(),
      "");
  Incidente incidente;

  @BeforeEach
  void setUp() {
    incidente = new Incidente(heladera, TipoIncidente.FALLA_CONEXION, ZonedDateTime.now());

    repositorioIncidentes.insert(incidente);
  }

  @AfterEach
  void tearDown() {
    new HibernatePersistenceReset().execute();
  }

  @Test
  void testVisitaSinResolucionNoResuelveIncidente() throws RepositoryException {
    repositorio.insert(new VisitaTecnica(mock(Tecnico.class), incidente, ZonedDateTime.now(), false));

    assertFalse(repositorio.getIsResuelto(incidente));
  }

  @Test
  void testVisitaResuelveIncidente() throws RepositoryException {
    repositorio.insert(new VisitaTecnica(mock(Tecnico.class), incidente, ZonedDateTime.now(), true));

    assertTrue(repositorio.getIsResuelto(incidente));
  }

  @Test
  void insertarVisitaParaIncidenteResueltoFalla() throws RepositoryException {
    repositorio.insert(new VisitaTecnica(mock(Tecnico.class), incidente, ZonedDateTime.now(), true));

    assertThrows(RepositoryException.class,
        () -> repositorio.insert(
            new VisitaTecnica(mock(Tecnico.class), incidente, ZonedDateTime.now(), true)));
  }
}