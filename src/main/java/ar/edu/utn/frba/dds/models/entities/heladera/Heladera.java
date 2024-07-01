package ar.edu.utn.frba.dds.models.entities.heladera;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Ubicacion;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Objects;

public class Heladera {
  @Getter
  final int capacidadEnViandas;
  final @NonNull ZonedDateTime fechaInstalacion;
  @Getter
  final @NonNull Colaborador encargado;
  @Getter
  final Ubicacion ubicacion;
  @Getter
  @Setter
  int id;
  @Getter
  @NonNull String nombre;
  @Getter
  double ultimaTempRegistradaCelsius;
  @Getter
  ZonedDateTime momentoUltimaTempRegistrada;

  public Heladera(String nombre,
                  Ubicacion ubicacion,
                  Colaborador encargado,
                  int capacidadEnViandas,
                  ZonedDateTime fechaInstalacion) {
    this.nombre = nombre;
    this.ubicacion = ubicacion;
    this.encargado = encargado;
    this.capacidadEnViandas = capacidadEnViandas;
    this.fechaInstalacion = fechaInstalacion;
  }

  public void setUltimaTempRegistradaCelsius(double temperatura) {
    ultimaTempRegistradaCelsius = temperatura;
    momentoUltimaTempRegistrada = ZonedDateTime.now();
  }
  private boolean ultimaTemperaturaEsVieja() {
    ZonedDateTime haceCincoMinutos = ZonedDateTime.now().minusMinutes(5);
    return momentoUltimaTempRegistrada.isBefore(haceCincoMinutos);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Heladera heladera = (Heladera) o;
    return getId() == heladera.getId();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
