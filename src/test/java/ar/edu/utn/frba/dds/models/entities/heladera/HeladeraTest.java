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
        0,
        10,
        5);
  }
  /* TODO Rehacer los test con el sensor mockeado
  @Test
  public void heladeraCalculaSusMesesActiva() {
    heladera.setUltimaTempRegistradaCelsius(5); // Temperatura dentro del rango deseado

    assertEquals(6, heladera.mesesActiva(), "Calcula sus meses activa");
  }

  @Test
  public void testEstadoHeladeraEnFalla() throws NoSuchFieldException, IllegalAccessException {
    heladera.setUltimaTempRegistradaCelsius(5); // Temperatura correcta

    // Usamos reflexión para manipular el campo privado momentoUltimaTempRegistrada
    Field field = Heladera.class.getDeclaredField("momentoUltimaTempRegistrada");
    field.setAccessible(true);
    field.set(heladera, ZonedDateTime.now().minusMinutes(10)); // Simula un registro antiguo

    assertEquals(EstadoDeFuncionamiento.EN_FALLA,
        heladera.getEstado(),
        "Está en falla si la última temperatura registrada es vieja");
  }

  @Test
  public void testEstadoHeladeraDeficiente() {
    heladera.setUltimaTempRegistradaCelsius(30); // Temperatura fuera del rango permitido

    assertEquals(EstadoDeFuncionamiento.DEFICIENTE,
        heladera.getEstado(),
        "Está en estado deficiente si la temperatura está fuera del rango permitido");
  }

  @Test
  public void testEstadoHeladeraReposando() {
    heladera.setUltimaTempRegistradaCelsius(5.5f); // Temperatura dentro del margen de tolerancia

    assertEquals(EstadoDeFuncionamiento.REPOSANDO,
        heladera.getEstado(),
        "Está reposando si la temperatura está dentro del margen de tolerancia");
  }

  @Test
  public void testEstadoHeladeraEnfriando() {
    heladera.setUltimaTempRegistradaCelsius(7); // Temperatura fuera del margen de tolerancia, pero dentro del rango permitido

    assertEquals(EstadoDeFuncionamiento.ENFRIANDO,
        heladera.getEstado(),
        "Está enfriando si la temperatura está fuera del margen de tolerancia pero dentro del rango permitido.");
  }
   */
}
