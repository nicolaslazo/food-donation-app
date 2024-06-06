package ar.edu.utn.frba.dds.models.entities.ubicacion;

import lombok.Getter;

import java.util.Objects;

public final class Ubicacion extends Coordenadas {
  @Getter
  private final double latitud;
  @Getter
  private final double longitud;
  private final String casa;
  private final String categoria;
  private final String cercaDe;
  private final String numeroDeCasa;
  private final String calle;
  private final String unidad;
  private final String piso;
  private final String escalera;
  private final String entrada;
  private final String cajaPostal;
  private final String codigoPostal;
  private final String suburbio;
  private final String distrito;
  private final String ciudad;
  private final String isla;
  private final String municipalidad;
  private final String provincia;
  private final String regionDePais;
  private final String pais;
  private final String regionDeMundo;

  public Ubicacion(double latitud,
                   double longitud,
                   String casa,
                   String categoria,
                   String cercaDe,
                   String numeroDeCasa,
                   String calle,
                   String unidad,
                   String piso,
                   String escalera,
                   String entrada,
                   String cajaPostal,
                   String codigoPostal,
                   String suburbio,
                   String distrito,
                   String ciudad,
                   String isla,
                   String municipalidad,
                   String provincia,
                   String regionDePais,
                   String pais,
                   String regionDeMundo) {
    super(latitud, longitud);

    this.latitud = latitud;
    this.longitud = longitud;
    this.casa = casa;
    this.categoria = categoria;
    this.cercaDe = cercaDe;
    this.numeroDeCasa = numeroDeCasa;
    this.calle = calle;
    this.unidad = unidad;
    this.piso = piso;
    this.escalera = escalera;
    this.entrada = entrada;
    this.cajaPostal = cajaPostal;
    this.codigoPostal = codigoPostal;
    this.suburbio = suburbio;
    this.distrito = distrito;
    this.ciudad = ciudad;
    this.isla = isla;
    this.municipalidad = municipalidad;
    this.provincia = provincia;
    this.regionDePais = regionDePais;
    this.pais = pais;
    this.regionDeMundo = regionDeMundo;
  }

  public Ubicacion(double latitud, double longitud) {
    this(latitud,
        longitud,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    Ubicacion ubicacion = (Ubicacion) o;
    return Double.compare(getLatitud(), ubicacion.getLatitud()) == 0 && Double.compare(getLongitud(), ubicacion.getLongitud()) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getLatitud(), getLongitud());
  }

  @Override
  public String toString() {
    return "Ubicacion[" +
        "latitud=" + latitud + ", " +
        "longitud=" + longitud + ", " +
        "casa=" + casa + ", " +
        "categoria=" + categoria + ", " +
        "cercaDe=" + cercaDe + ", " +
        "numeroDeCasa=" + numeroDeCasa + ", " +
        "calle=" + calle + ", " +
        "unidad=" + unidad + ", " +
        "piso=" + piso + ", " +
        "escalera=" + escalera + ", " +
        "entrada=" + entrada + ", " +
        "cajaPostal=" + cajaPostal + ", " +
        "codigoPostal=" + codigoPostal + ", " +
        "suburbio=" + suburbio + ", " +
        "distrito=" + distrito + ", " +
        "ciudad=" + ciudad + ", " +
        "isla=" + isla + ", " +
        "municipalidad=" + municipalidad + ", " +
        "provincia=" + provincia + ", " +
        "regionDePais=" + regionDePais + ", " +
        "pais=" + pais + ", " +
        "regionDeMundo=" + regionDeMundo + ']';
  }
}
