package ar.edu.utn.frba.dds.models.entities.users;

import lombok.NonNull;

public enum TipoPersonaJuridica {
  EMPRESA,
  GUBERNAMENTAL,
  INSTITUCION,
  ONG;

  public static TipoPersonaJuridica fromString(@NonNull String tipo) {
    return switch (tipo) {
      case "EMPRESA" -> EMPRESA;
      case "GUBERNAMENTAL" -> GUBERNAMENTAL;
      case "INSTITUCION" -> INSTITUCION;
      case "ONG" -> ONG;
      default -> null;
    };
  }
}