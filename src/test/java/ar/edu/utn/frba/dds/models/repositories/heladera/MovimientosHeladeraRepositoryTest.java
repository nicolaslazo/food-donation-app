package ar.edu.utn.frba.dds.models.repositories.heladera;


import ar.edu.utn.frba.dds.models.entities.heladera.EventoMovimiento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import java.time.ZonedDateTime;
import java.util.List;

class MovimientosHeladeraRepositoryTest {
  MovimientosHeladeraRepository repositorio;
  @Mock
  private Heladera mockHeladera;

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
  void setUpMovimientoSemana() {
    MockitoAnnotations.openMocks(this);
    repositorio = MovimientosHeladeraRepository.getInstancia();

    ZonedDateTime now = ZonedDateTime.now();
    // Limpiamos los movimientos para evitar interferencias con otras pruebas
    repositorio.obtenerMovimientosSemanaAnterior().clear();
    // Movimientos dentro de la semana anterior
    repositorio.insert(new EventoMovimiento(mockHeladera, now.minusDays(3)));
    repositorio.insert(new EventoMovimiento(mockHeladera, now.minusDays(5)));
    // Movimientos fuera de la semana anterior
    repositorio.insert(new EventoMovimiento(mockHeladera, now.minusDays(10)));
    repositorio.insert(new EventoMovimiento(mockHeladera, now.minusDays(15)));
  }
  @Test
  void testObtenerMovimientosSemanaAnterior() {
    setUpMovimientoSemana();
    List<EventoMovimiento> resultado = repositorio.obtenerMovimientosSemanaAnterior();

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
}
