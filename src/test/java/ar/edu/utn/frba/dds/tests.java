package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.auth.EstandarDeContrasena;
import ar.edu.utn.frba.dds.auth.ExcepcionDeValidacionDeContrasena;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;

public class tests {

  @Test
  public void testContrasenaCorta() {
    String contrasenaCorta = "short";
    ExcepcionDeValidacionDeContrasena exception = assertThrows(
        ExcepcionDeValidacionDeContrasena.class,
        () -> EstandarDeContrasena.validar(contrasenaCorta));
    assertEquals("La contraseña debe ser de al menos 8 caracteres", exception.getMessage());
  }

  @Test
  public void testContrasenaValida() throws ExcepcionDeValidacionDeContrasena {
    String contrasenaValida = "Holaa1234!";
    Set<String> alertas = EstandarDeContrasena.validar(contrasenaValida);
    assertTrue(alertas.isEmpty());
  }

  @Test
  public void testContrasenaUnicode() {
    String contrasenaUnicode= "Canc1ión";
    assertTrue(EstandarDeContrasena.contieneCaracteresUnicode(contrasenaUnicode));
  }
  @Test
  public void testContrasenaNoUnicode() {
    String contrasenaUnicode= "Canc1ioon";
    assertFalse(EstandarDeContrasena.contieneCaracteresUnicode(contrasenaUnicode));
  }

  @Test
  public void testEsContrasenaNoPopular() {
    String contrasenaNoExistente = "ContraseñaUnicaa1";
    assertFalse(EstandarDeContrasena.esContrasenaPopular(contrasenaNoExistente));
  }

  @Test
  public void testEsContrasenaPopular() {
    // Prueba con una contraseña que existe en el archivo
    String contrasenaExistente = "master";
    assertTrue(EstandarDeContrasena.esContrasenaPopular(contrasenaExistente));
  }
  @Test
  public void testContrasenaInvalidaPopular() {
    String contrasenaPopular = "qwertyuiop";
    ExcepcionDeValidacionDeContrasena exception = assertThrows(
        ExcepcionDeValidacionDeContrasena.class,
        () -> EstandarDeContrasena.validar(contrasenaPopular));
    assertEquals("La contraseña es demasiado popular. Por favor elija una contraseña más única", exception.getMessage());
  }

}
