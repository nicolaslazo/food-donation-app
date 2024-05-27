package ar.edu.utn.frba.dds.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class GeneradorContraseniaTest {

  @Test
  public void testGeneraContrasenia() {
    String contrasenia = GeneradorDeContrasenias.generarContrasenia();

    // Verifica que la contraseña no sea nula
    assertNotNull(contrasenia, "La contraseña generada no debe ser nula");
  }

  @Test
  public void testLongitudContrasenia() {
    String contrasenia = GeneradorDeContrasenias.generarContrasenia();
    assertEquals(12, contrasenia.length(), "La longitud de la contraseña debe ser 12");
  }
}
