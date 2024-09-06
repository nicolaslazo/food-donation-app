package ar.edu.utn.frba.dds.models.repositories.heladera;


import ar.edu.utn.frba.dds.models.entities.heladera.EventoMovimiento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.HibernatePersistenceReset;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MovimientosHeladeraRepositoryTest {
  MovimientosHeladeraRepository repositorio = new MovimientosHeladeraRepository();
  EventoMovimiento heladeraMock = mock(EventoMovimiento.class);
  
  @BeforeEach
  void setUp() {
    repositorio.insert(heladeraMock);
  }

  @AfterEach
  void tearDown() {
    new HibernatePersistenceReset().execute();
  }
  @Test
  void repositorioSeInstancia() {
    assertInstanceOf(MovimientosHeladeraRepository.class, repositorio);
  }

  @Test
  void testInsert() {
    assertNotNull(heladeraMock.getId());
  }

}
