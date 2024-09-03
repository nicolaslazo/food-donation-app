package ar.edu.utn.frba.dds.models.entities.heladera;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Embedded;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Entity
@Table(name = "heladera")
public class Heladera {
  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false, unique = true, updatable = false)
  @Getter
  @Setter
  Long id;

  @Column(name = "nombre", nullable = false)
  @Getter
  @NonNull String nombre;

  @Column(name = "capacidadEnViandas")
  @Getter
  Integer capacidadEnViandas;

  @Column(name = "fechaInstalacion", nullable = false, columnDefinition = "DATETIME")
  @NonNull ZonedDateTime fechaInstalacion;

  @ManyToOne
  @JoinColumn(name = "idColaborador", referencedColumnName = "id")
  @Getter
  @NonNull Colaborador encargado;

  @Embedded
  @Getter
  CoordenadasGeograficas ubicacion;

  @Column(name = "ultimaTemperaturaRegistradaEnCelsius")
  @Getter
  Double ultimaTempRegistradaCelsius;

  @Column(name = "momentoDeUltimaTempRegistrada", columnDefinition = "DATETIME")
  @Getter
  ZonedDateTime momentoUltimaTempRegistrada;

  public Heladera(String nombre,
                  CoordenadasGeograficas ubicacion,
                  Colaborador encargado,
                  int capacidadEnViandas,
                  ZonedDateTime fechaInstalacion) {
    this.nombre = nombre;
    this.ubicacion = ubicacion;
    this.encargado = encargado;
    this.capacidadEnViandas = capacidadEnViandas;
    this.fechaInstalacion = fechaInstalacion;
  }

  protected Heladera() {
  }

  public void setUltimaTempRegistradaCelsius(double temperatura) {
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
