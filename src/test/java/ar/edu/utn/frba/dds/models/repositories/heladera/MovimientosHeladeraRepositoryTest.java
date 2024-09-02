package ar.edu.utn.frba.dds.models.repositories.heladera;


import ar.edu.utn.frba.dds.models.entities.heladera.EventoMovimiento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class MovimientosHeladeraRepositoryTest {
  MovimientosHeladeraRepository repositorio = MovimientosHeladeraRepository.getInstancia();
  Heladera heladeraMock = mock(Heladera.class);

  @AfterEach
  void tearDown() {
    repositorio.deleteTodos();
  }

  @Test
  void repositorioSeInstancia() {
    assertInstanceOf(MovimientosHeladeraRepository.class, repositorio);
  }

  @Test
  void testInsert() {
    EventoMovimiento evento = new EventoMovimiento(mock(Heladera.class), ZonedDateTime.now());

    assertEquals(1, repositorio.insert(evento));
  }

}
