package ar.edu.utn.frba.dds.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ValidadorContraseniaTest {
  @Test
  public void testContraseniaCorta() {
    ValidadorDeContrasenia validador = new ValidadorDeContrasenia();
    assertFalse(validador.validar("hola"));
  }

  @Test
  public void testContraseniaUnicode() {
    ValidadorDeContrasenia validador = new ValidadorDeContrasenia();
    assertFalse(validador.validar("ÁÁÁÁÁÁ"));
  }

  @Test
  public void testContraseniaPopular() {
    ValidadorDeContrasenia validador = new ValidadorDeContrasenia();
    assertFalse(validador.validar("qwertyuiop"));
  }

  @Test
  public void testContraseniaValida() {
    ValidadorDeContrasenia validador = new ValidadorDeContrasenia();
    assertTrue(validador.validar("contraseniaValida"));
  }

  @Test
  public void testContraseniaGenerada() {
    ValidadorDeContrasenia validador = new ValidadorDeContrasenia();
    String contrasenia = GeneradorDeContrasenias.generarContrasenia();
    assertTrue(validador.validar(contrasenia));
  }
}
