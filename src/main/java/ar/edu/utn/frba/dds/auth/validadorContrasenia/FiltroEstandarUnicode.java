package ar.edu.utn.frba.dds.auth.validadorContrasenia;

import lombok.NonNull;


public class FiltroEstandarUnicode implements  FiltrosContrasenia{
  @Override
  public boolean validar(@NonNull String contrasena) {
    return contrasena.chars().anyMatch(c -> c > 127);
  }
}
