package ar.edu.utn.frba.dds.models.repositories.heladera;

import ar.edu.utn.frba.dds.models.entities.heladera.EventoMovimiento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class MovimientosHeladeraRepositoryTest {
  MovimientosHeladeraRepository repositorio;

  @BeforeEach
  void setUp() {
    repositorio = new MovimientosHeladeraRepository();
  }

  @Test
  void repositorioSeInstancia() {
    assertInstanceOf(MovimientosHeladeraRepository.class, repositorio);
  }

  @Test
  void testInsert() {
    EventoMovimiento evento = new EventoMovimiento(Mockito.mock(Heladera.class), ZonedDateTime.now());

    assertEquals(1, repositorio.insert(evento));
  }
}