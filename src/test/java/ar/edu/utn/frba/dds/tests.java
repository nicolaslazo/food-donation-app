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

  /**PROBLEMA CON LECTURA DE TOP1000CONTRASENAS. CUANDO LLEGA AHÍ SE ROMPE*/
  @Test
  public void testContrasenaValida() throws ExcepcionDeValidacionDeContrasena {
    String contrasenaValida = "Holaaaaaa";
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

  /**PROBLEMA CON LECTURA DEL ARCHIVO, NO ANDA DIRECTAMENTE EL MÉTODO*/
  @Test
  public void testContrasenaPopular() {
    String contrasenaPopular= "123456789";
    assertFalse(EstandarDeContrasena.esContrasenaPopular(contrasenaPopular));
  }

}
