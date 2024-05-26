package ar.edu.utn.frba.dds.domain.ubicacion;

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
}
