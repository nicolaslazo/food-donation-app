package ar.edu.utn.frba.dds.controllers.heladera;

import ar.edu.utn.frba.dds.models.entities.heladera.EventoMovimiento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovimientosHeladeraControllerTest {
  @Mock
  private Heladera heladera1;

  @Mock
  private Heladera heladera2;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testMapCantidadMovimientosPorHeladeraSemanaAnterior() {
    List<EventoMovimiento> eventos = new ArrayList<>();

    // Mock Heladera
    when(heladera1.getNombre()).thenReturn("Heladera 1");
    when(heladera2.getNombre()).thenReturn("Heladera 2");

    // Create mock EventoMovimiento
    EventoMovimiento evento1 = new EventoMovimiento(heladera1, ZonedDateTime.now().minusDays(10));
    EventoMovimiento evento2 = new EventoMovimiento(heladera1, ZonedDateTime.now().minusDays(2));
    EventoMovimiento evento3 = new EventoMovimiento(heladera2, ZonedDateTime.now().minusDays(1));

    eventos.add(evento1);
    eventos.add(evento2);
    eventos.add(evento3);

    Map<String, Integer> expected = new HashMap<>();
    expected.put("Heladera 1", 1);
    expected.put("Heladera 2", 1);

    assertEquals(expected, MovimientosHeladeraController.getInstance().MapCantidadMovimientosPorHeladeraSemanaAnterior(eventos));
  }
}
