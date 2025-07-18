package ar.edu.utn.frba.dds.models.entities.documentacion;

import lombok.NonNull;

public enum TipoDocumento {
  DNI, LIBRETA_CIVICA, LIBRETA_DE_ENROLAMIENTO, CUIT;

  public static TipoDocumento fromString(@NonNull String tipo) {
    return switch (tipo) {
      case "DNI" -> DNI;
      case "LC" -> LIBRETA_CIVICA;
      case "LE" -> LIBRETA_DE_ENROLAMIENTO;
      case "CUIT" -> CUIT;
      default -> null;
    };
  }
}