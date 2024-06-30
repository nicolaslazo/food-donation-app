package ar.edu.utn.frba.dds.models.entities.heladera.incidente;

import lombok.NonNull;

public enum TipoIncidente {
  FALLA_REPORTADA_POR_COLABORADOR,
  BAJA_TEMPERATURA,
  ALTA_TEMPERATURA,
  FALLA_CONEXION,
  FRAUDE;

  public static TipoIncidente fromString(@NonNull String tipo) throws Exception {
    return switch (tipo) {
      case "FALLA_REPORTADA_POR_COLABORADOR" -> FALLA_REPORTADA_POR_COLABORADOR;
      case "BAJA_TEMPERATURA" -> BAJA_TEMPERATURA;
      case "ALTA_TEMPERATURA" -> ALTA_TEMPERATURA;
      case "FALLA_CONEXION" -> FALLA_CONEXION;
      case "FRAUDE" -> FRAUDE;
      default -> {
        throw new Exception("Tipo de incidente no reconocido");
      }
    };
  }
}
