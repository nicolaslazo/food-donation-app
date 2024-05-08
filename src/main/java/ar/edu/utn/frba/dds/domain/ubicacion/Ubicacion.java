package ar.edu.utn.frba.dds.domain.ubicacion;

public class Ubicacion {
  private String casa;
  private String categoria;
  private String cercaDe;
  private String numeroDeCasa;
  private String calle;
  private String unidad;
  private String piso;
  private String escalera;
  private String entrada;
  private String cajaPostal;
  private String codigoPostal;
  private String suburbio;
  private String distrito;
  private String ciudad;
  private String isla;
  private String municipalidad;
  private String provincia;
  private String regionDePais;
  private String pais;
  private String regionDeMundo;
  private Coordenadas coordenadas;

  public Ubicacion() {
  }

  public static Ubicacion desdeDireccion(String direccion) {
    // TODO implement here
    return null;
  }
}