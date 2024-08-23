package ar.edu.utn.frba.dds.models.entities.ubicacion;

import lombok.NonNull;

public class CalculadoraDistancia {
  private static final double RADIO_DE_TIERRA_EN_METROS = 6371000;

  public static Double calcular(@NonNull CoordenadasGeograficas uno, @NonNull CoordenadasGeograficas otro) {
    /*
     * Basada en la fórmula de Haversine
     */
    final double distanciaLatitud = Math.toRadians(otro.latitud() - uno.latitud());
    final double distanciaLongitud = Math.toRadians(otro.longitud() - uno.longitud());

    final double unaLatitud = Math.toRadians(uno.latitud());
    final double otraLatitud = Math.toRadians(otro.latitud());

    final double a = Math.pow(Math.sin(distanciaLatitud / 2), 2)
        + Math.pow(Math.sin(distanciaLongitud / 2), 2) * Math.cos(unaLatitud) * Math.cos(otraLatitud);
    final double c = 2 * Math.asin(Math.sqrt(a));

    return RADIO_DE_TIERRA_EN_METROS * c;
  }
}
