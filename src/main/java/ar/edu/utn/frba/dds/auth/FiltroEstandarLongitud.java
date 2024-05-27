package ar.edu.utn.frba.dds.auth;

import lombok.NonNull;

public class FiltroEstandarLongitud implements FiltrosContrasenia {
  @Override
  public boolean validar(@NonNull String contrasenia) {
    return contrasenia.length() >= 8;
  }
}


