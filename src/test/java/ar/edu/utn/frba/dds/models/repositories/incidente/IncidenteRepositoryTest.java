package ar.edu.utn.frba.dds.models.repositories.incidente;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.Incidente;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.TipoIncidente;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.repositories.HibernatePersistenceReset;
import ar.edu.utn.frba.dds.models.repositories.heladera.incidente.IncidenteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IncidenteRepositoryTest {
  final IncidenteRepository incidenteRepository = new IncidenteRepository();
  Colaborador colaborador = new Colaborador(new Documento(TipoDocumento.DNI, 1),
      "",
      "",
      LocalDate.now(),
      new CoordenadasGeograficas(-34d, -58d));
  Heladera heladera = new Heladera("",
      new CoordenadasGeograficas(-34d, -58d),
      colaborador,
      1,
      ZonedDateTime.now());
  Incidente incidente;

  @BeforeEach
  public void setUp() {
    incidente = new Incidente(
        heladera,
        TipoIncidente.BAJA_TEMPERATURA,
        ZonedDateTime.now(),
        colaborador,
        "",
        null
    );
  }

  @AfterEach
  void tearDown() {
    new HibernatePersistenceReset().execute();
  }

  @Test
  public void testInsertIncidenteHeladera() {
    new IncidenteRepository().insert(incidente);

    assertEquals(1, incidenteRepository.findAll().count());
    assertNotNull(incidente.getId());
  }

  @Test
  public void testGetIncidenteHeladera() {
    incidenteRepository.insert(incidente);

    Optional<Incidente> foundIncidente = incidenteRepository.findById(incidente.getId());
    assertEquals(incidente, foundIncidente.get());
  }

  @Test
  public void testExistsIncidenteHeladera() {
    incidenteRepository.insert(incidente);
    assertTrue(incidenteRepository.findById(incidente.getId()).isPresent());
  }

  @Test
  public void testDeleteTodos() {
    incidenteRepository.insert(incidente);
    incidenteRepository.deleteAll();
    assertEquals(0, incidenteRepository.findAll().count());
  }
}
