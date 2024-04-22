package ar.edu.utn.frba.dds.auth;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import lombok.NonNull;

/**
 * Contiene el conocimiento para verificar la validez de una contraseña.
 */
public class EstandarDeContrasena {
  public static boolean contieneCaracteresUnicode(@NonNull String contrasena) {
    return contrasena.chars().anyMatch(c -> c > 127);
  }

  public static boolean esContrasenaPopular(@NonNull String contrasena) {
    try (Stream<String> lineas = Files.lines(
            Paths.get("src/main/java/ar/edu/utn/frba/dds/auth/top-10000-contrasenas.txt")))
    {
      return lineas.anyMatch(linea -> linea.equals(contrasena));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Asegura que la contraseña se alinee con las recomendaciones de la Sección 5.1.1.2 para
   * Secretos Memorizados de la Guía NIST.
   * Tira una ExcepcionDeValidacionDeContrasena con el justificativo en el caso de un rechazo.
   *
   * @param contrasena la contraseña a verificar
   * @return TODO: asegurarnos que existe una chance que más de una alerta se pueda devolver
   * @throws ExcepcionDeValidacionDeContrasena reporta el rechazo de una contraseña
   */
  public static Set<String> validar(@NonNull String contrasena)
      throws ExcepcionDeValidacionDeContrasena {
    HashSet<String> alertas = new HashSet<>();

    if (contrasena.codePointCount(0, contrasena.length()) < 8) {
      throw new ExcepcionDeValidacionDeContrasena(
          "La contraseña debe ser de al menos 8 caracteres"
      );
    }

    if (EstandarDeContrasena.contieneCaracteresUnicode(contrasena)) {
      alertas.add(
          "Incluir caracteres especiales puede afectar la capacidad de autenticarse correctamente"
      );
    }

    if (EstandarDeContrasena.esContrasenaPopular(contrasena)) {
      throw new ExcepcionDeValidacionDeContrasena(
          "La contraseña es demasiado popular. Por favor elija una contraseña más única"
      );
    }

    return alertas;
  }
}
