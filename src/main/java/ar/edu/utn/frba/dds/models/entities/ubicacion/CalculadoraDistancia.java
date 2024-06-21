package ar.edu.utn.frba.dds.models.entities.ubicacion;

import lombok.NonNull;

public class CalculadoraDistancia {
  private static final double RADIO_DE_TIERRA_EN_METROS = 6371000;

  public static double calcular(@NonNull Coordenadas uno, @NonNull Coordenadas otro) {
    /*
     * Basada en la fórmula de Haversine
     */
    final double distanciaLatitud = Math.toRadians(otro.getLatitud() - uno.getLatitud());
    final double distanciaLongitud = Math.toRadians(otro.getLongitud() - uno.getLongitud());

    final double unaLatitud = Math.toRadians(uno.getLatitud());
    final double otraLatitud = Math.toRadians(otro.getLatitud());

    final double a = Math.pow(Math.sin(distanciaLatitud / 2), 2)
        + Math.pow(Math.sin(distanciaLongitud / 2), 2) * Math.cos(unaLatitud) * Math.cos(otraLatitud);
    final double c = 2 * Math.asin(Math.sqrt(a));

    return RADIO_DE_TIERRA_EN_METROS * c;
  }
}
