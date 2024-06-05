package ar.edu.utn.frba.dds.models.entities.ubicacion;

import lombok.Getter;
import lombok.NonNull;

import java.util.Objects;

@SuppressWarnings("ClassCanBeRecord")
@Getter
public final class Coordenadas {
  private final @NonNull Double longitud;
  private final @NonNull Double latitud;

  public Coordenadas(@NonNull Double longitud, @NonNull Double latitud) {
    this.longitud = longitud;
    this.latitud = latitud;
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
