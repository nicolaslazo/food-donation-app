package ar.edu.utn.frba.dds.models.entities.ubicacion;

import java.util.Objects;

public record Ubicacion(
    Coordenadas coordenadas, String casa, String categoria, String cercaDe, String numeroDeCasa, String calle,
    String unidad, String piso,
    String escalera, String entrada, String cajaPostal, String codigoPostal, String suburbio, String distrito,
    String ciudad, String isla, String municipalidad, String provincia, String regionDePais, String pais,
    String regionDeMundo
) {
  public Ubicacion(Coordenadas coordenadas) {
    this(coordenadas, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null,
        null, null, null, null, null);
  }

  public static Ubicacion desdeDireccion(String direccion) {
    // TODO: implement here
    return null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Ubicacion ubicacion = (Ubicacion) o;
    return Objects.equals(coordenadas, ubicacion.coordenadas);
  }

  @Override
  public int hashCode() {
    return Objects.hash(coordenadas);
  }
}
