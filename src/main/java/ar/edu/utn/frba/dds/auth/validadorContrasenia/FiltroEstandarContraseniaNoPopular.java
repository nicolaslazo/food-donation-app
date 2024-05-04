package ar.edu.utn.frba.dds.auth.validadorContrasenia;

import lombok.NonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FiltroEstandarContraseniaNoPopular implements FiltrosContrasenia{
  public boolean validar(@NonNull String contrasena) {
    try (Stream<String> lineas = Files.lines(
        Paths.get("src/main/java/ar/edu/utn/frba/dds/auth/validadorContrasenia/top-10000-contrasenas.txt")))
    {return !(lineas.anyMatch(linea -> linea.equals(contrasena)));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
