package ar.edu.utn.frba.dds.auth;

import lombok.NonNull;


public class FiltroEstandarNoTieneUnicode implements FiltrosContrasenia {
  @Override
  public boolean validar(@NonNull String contrasena) {
    return contrasena.chars().noneMatch(c -> c > 127);
  }
}
