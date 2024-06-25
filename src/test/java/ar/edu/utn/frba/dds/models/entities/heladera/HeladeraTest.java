package ar.edu.utn.frba.dds.models.entities.heladera;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HeladeraTest {
  private Heladera heladera;

  @BeforeEach
  void setUp() {
    heladera = new Heladera("Heladera Test",
        null,
        Mockito.mock(Colaborador.class),
        10,
        ZonedDateTime.now().minusMonths(6),
        true
        );
  }

  @Test
  public void heladeraCalculaSusMesesActiva() {
    heladera.setUltimaTempRegistradaCelsius(5); // Temperatura dentro del rango deseado

    assertEquals(6, heladera.mesesActiva(), "Calcula sus meses activa");
  }



}
