package ar.edu.utn.frba.dds.models.entities.ubicacion;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Coordenadas {
  private final double latitud;
  private final double longitud;

  public Coordenadas(double latitud, double longitud) {
    this.latitud = latitud;
    this.longitud = longitud;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Coordenadas that = (Coordenadas) o;
    return Objects.equals(getLongitud(), that.getLongitud()) && Objects.equals(getLatitud(), that.getLatitud());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getLongitud(), getLatitud());
  }

  @Override
  public String toString() {
    return "Coordenadas{" +
        "longitud=" + longitud +
        ", latitud=" + latitud +
        '}';
  }
}
