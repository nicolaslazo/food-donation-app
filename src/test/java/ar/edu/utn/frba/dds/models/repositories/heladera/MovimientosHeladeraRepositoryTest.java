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

  void setUpMovimientoSemana() {
    MockitoAnnotations.openMocks(this);
    repositorio = MovimientosHeladeraRepository.getInstancia();

    ZonedDateTime now = ZonedDateTime.now();
    // Movimientos dentro de la semana anterior
    repositorio.insert(new EventoMovimiento(heladeraMock, now.minusDays(3)));
    repositorio.insert(new EventoMovimiento(heladeraMock, now.minusDays(5)));
    // Movimientos fuera de la semana anterior
    repositorio.insert(new EventoMovimiento(heladeraMock, now.minusDays(10)));
    repositorio.insert(new EventoMovimiento(heladeraMock, now.minusDays(15)));
  }

  @Test
  void testObtenerMovimientosSemanaAnterior() {
    setUpMovimientoSemana();
    List<EventoMovimiento> resultado = repositorio.getMovimientosSemanaAnterior();

    // Verificamos que solo haya dos movimientos en el resultado
    assertEquals(2, resultado.size());

    ZonedDateTime fechaActual = ZonedDateTime.now();
    ZonedDateTime inicioSemanaAnterior = fechaActual.minusWeeks(1);

    // Verificamos que los movimientos están dentro del rango correcto
    for (EventoMovimiento movimiento : resultado) {
      assertTrue(movimiento.getFecha().isAfter(inicioSemanaAnterior));
      assertTrue(movimiento.getFecha().isBefore(fechaActual));
    }
  }

  @Test
  void testReportaCantidadMovimientosPorHeladera() {
    Heladera otraHeladeraMock = mock(Heladera.class);

    repositorio.insert(new EventoMovimiento(heladeraMock, ZonedDateTime.now()));
    repositorio.insert(new EventoMovimiento(otraHeladeraMock, ZonedDateTime.now()));
    repositorio.insert(new EventoMovimiento(otraHeladeraMock, ZonedDateTime.now()));

    Map<Heladera, Integer> cantidadesDeMovimientosPorHeladera =
        repositorio.getCantidadMovimientosPorHeladeraSemanaAnterior();

    assertEquals(1, cantidadesDeMovimientosPorHeladera.get(heladeraMock));
    assertEquals(2, cantidadesDeMovimientosPorHeladera.get(otraHeladeraMock));
  }
}
