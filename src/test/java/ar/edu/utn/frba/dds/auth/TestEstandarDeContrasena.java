package ar.edu.utn.frba.dds.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;

public class TestEstandarDeContrasena {

  @Test
  public void testContrasenaCorta() {
    String contrasenaCorta = "short";
    ExcepcionDeValidacionDeContrasena exception = assertThrows(
        ExcepcionDeValidacionDeContrasena.class,
        () -> EstandarDeContrasena.validar(contrasenaCorta));
    assertEquals("La contraseña debe ser de al menos 8 caracteres", exception.getMessage());
  }

  @Test
  public void testContrasenaCortaUnicode() {
    String contrasenaCortaUnicode = "ÁÁÁÁÁÁ";
    ExcepcionDeValidacionDeContrasena exception = assertThrows(
        ExcepcionDeValidacionDeContrasena.class,
        () -> EstandarDeContrasena.validar(contrasenaCortaUnicode));
    assertEquals("La contraseña debe ser de al menos 8 caracteres", exception.getMessage());
  }

  @Test
  public void testContrasenaValida() throws ExcepcionDeValidacionDeContrasena {
    String contrasenaValida = "Holaa1234!";
    Set<String> alertas = EstandarDeContrasena.validar(contrasenaValida);
    assertTrue(alertas.isEmpty());
  }
  @Test
  public void testContrasenaInvalidaPopular() {
    String contrasenaPopular = "qwertyuiop";
    ExcepcionDeValidacionDeContrasena exception = assertThrows(
        ExcepcionDeValidacionDeContrasena.class,
        () -> EstandarDeContrasena.validar(contrasenaPopular));
    assertEquals("La contraseña es demasiado popular. Por favor elija una contraseña más única", exception.getMessage());
  }

  @Test
  public void testContrasenaConUnicode() {
    String contrasenaConUnicode = "Camiónavión";
    try {
      Set<String> alertas = EstandarDeContrasena.validar(contrasenaConUnicode);
      assertTrue(alertas.contains("Incluir caracteres especiales puede afectar la capacidad de autenticarse correctamente"));
    } catch (ExcepcionDeValidacionDeContrasena e) {
      fail("La contraseña con Unicode no debería lanzar una excepción de validación");
    }
  }



}
