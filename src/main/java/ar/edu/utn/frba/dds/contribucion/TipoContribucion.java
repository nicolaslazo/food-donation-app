package ar.edu.utn.frba.dds.contribucion;

public enum TipoContribucion {
  DINERO, DONACION_VIANDAS, ENTREGA_TARJETAS, REDISTRIBUCION_VIANDAS;

  public static TipoContribucion fromString(String tipoContribucion) {
    return switch(tipoContribucion) {
      case "DINERO" -> DINERO;
      case "DONACION_VIANDAS" -> DONACION_VIANDAS;
      case "ENTREGA_TARJETAS" -> ENTREGA_TARJETAS;
      case "REDISTRIBUCION_VIANDAS" -> REDISTRIBUCION_VIANDAS;
      default -> null;
    };
  }
}
