package ar.edu.utn.frba.dds.models.entities.heladera;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@ToString
@Entity
@Table(name = "heladera", uniqueConstraints = {@UniqueConstraint(columnNames = {"latitud", "longitud"})})
public class Heladera {
  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false, unique = true, updatable = false)
  @Getter
  Long id;

  @Column(name = "nombre", nullable = false)
  @Getter
  @NonNull String nombre;

  @Enumerated(EnumType.STRING)
  @Column(name = "modeloHeladera", updatable = false)
  ModeloHeladera modeloHeladera;

  @Column(name = "capacidadEnViandas", nullable = false, updatable = false)
  @Getter
  @NonNull Integer capacidadEnViandas;

  @Column(name = "fechaInstalacion", nullable = false, updatable = false)
  @NonNull ZonedDateTime fechaInstalacion;

  @ManyToOne
  @JoinColumn(name = "idColaborador", referencedColumnName = "idUsuario", nullable = false, updatable = false)
  @Getter
  @NonNull Colaborador encargado;

  @Embedded
  @Getter
  CoordenadasGeograficas ubicacion;

  @Column(name = "barrio", nullable = false, updatable = false)
  @Getter
  @NonNull String barrio;

  @Column(name = "ultimaTemperaturaRegistradaEnCelsius")
  @Getter
  Double ultimaTempRegistradaCelsius;

  @Column(name = "momentoDeUltimaTempRegistrada")
  @Getter
  ZonedDateTime momentoUltimaTempRegistrada;

  public Heladera(String nombre,
                  CoordenadasGeograficas ubicacion,
                  Colaborador encargado,
                  Integer capacidadEnViandas,
                  ZonedDateTime fechaInstalacion,
                  String barrio) {
    this.nombre = nombre;
    this.ubicacion = ubicacion;
    this.encargado = encargado;
    this.capacidadEnViandas = capacidadEnViandas;
    this.fechaInstalacion = fechaInstalacion;
    this.barrio = barrio;
  }

  public Heladera(String nombre,
                  CoordenadasGeograficas ubicacion,
                  Colaborador encargado,
                  ModeloHeladera modeloHeladera,
                  ZonedDateTime fechaInstalacion,
                  String barrio) {
    this.nombre = nombre;
    this.ubicacion = ubicacion;
    this.encargado = encargado;
    this.modeloHeladera = modeloHeladera;
    this.fechaInstalacion = fechaInstalacion;
    this.barrio = barrio;
    switch (modeloHeladera) {
      case HELADERA_CHICA -> this.capacidadEnViandas = 10;
      case HELADERA_MEDIANA -> this.capacidadEnViandas = 20;
      case HELADERA_GRANDE -> this.capacidadEnViandas = 30;
      default -> throw new RuntimeException("Unsupported modeloHeladera");
    }
  }

  protected Heladera() {
  }

  public void setUltimaTempRegistradaCelsius(Double temperatura) {
    ultimaTempRegistradaCelsius = temperatura;
    momentoUltimaTempRegistrada = ZonedDateTime.now();
  }

  private boolean ultimaTemperaturaEsVieja() {
    ZonedDateTime haceCincoMinutos = ZonedDateTime.now().minusMinutes(5);
    return momentoUltimaTempRegistrada.isBefore(haceCincoMinutos);
  }

  public int mesesActiva() {
    //if (!heladeraActiva) return 0;
    return (int) ChronoUnit.MONTHS.between(fechaInstalacion, ZonedDateTime.now());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Heladera heladera = (Heladera) o;
    return Objects.equals(getId(), heladera.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
