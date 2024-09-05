package ar.edu.utn.frba.dds.auth;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import lombok.NonNull;

public class FiltroEstandarContraseniaNoPopular implements FiltrosContrasenia {
  public boolean validar(@NonNull String contrasena) {
    try (Stream<String> lineas = Files.lines(
        Paths.get("src/main/java/ar/edu/utn/frba/dds/auth/top-10000-contrasenas.txt"))) {
      return lineas.noneMatch(linea -> linea.equals(contrasena));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
