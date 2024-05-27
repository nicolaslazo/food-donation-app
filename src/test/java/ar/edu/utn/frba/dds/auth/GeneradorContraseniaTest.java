package ar.edu.utn.frba.dds.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class GeneradorContraseniaTest {

  @Test
  public void testGeneraContrasenia() {
    GeneradorDeContrasenias generador = new GeneradorDeContrasenias();
    String contrasenia = generador.generarContrasenias();

    // Verifica que la contraseña no sea nula
    assertNotNull(contrasenia, "La contraseña generada no debe ser nula");
  }

  @Test
  public void testLongitudContrasenia() {
    GeneradorDeContrasenias generador = new GeneradorDeContrasenias();
    String contrasenia = generador.generarContrasenias();
    assertEquals(12, contrasenia.length(), "La longitud de la contraseña debe ser 12");
  }

  @Test
  public void testContieneMinusculas() {
    GeneradorDeContrasenias generador = new GeneradorDeContrasenias();
    String contrasenia = generador.generarContrasenias();
    assertTrue(contrasenia.chars().anyMatch(Character::isLowerCase),
        "La contraseña debe contener al menos una letra minúscula");
  }

  @Test
  public void testContieneMayusculas() {
    GeneradorDeContrasenias generador = new GeneradorDeContrasenias();
    String contrasenia = generador.generarContrasenias();
    assertTrue(contrasenia.chars().anyMatch(Character::isUpperCase),
        "La contraseña debe contener al menos una letra mayúscula");
  }

}
