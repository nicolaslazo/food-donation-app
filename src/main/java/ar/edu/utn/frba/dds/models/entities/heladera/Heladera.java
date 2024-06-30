package ar.edu.utn.frba.dds.models.entities.heladera;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Ubicacion;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Heladera {
  final double temperaturaMinimaCelsius;
  final double temperaturaMaximaCelsius;
  @Getter
  final int capacidadEnViandas;
  final @NonNull ZonedDateTime fechaInstalacion;
  @Getter
  final @NonNull Colaborador encargado;
  @Getter
  final @NonNull Ubicacion ubicacion;
  @Getter
  @Setter
  int id;
  @NonNull String nombre;
  double temperaturaDeseadaCelsius;
  Double ultimaTempRegistradaCelsius;
  @Getter
  ZonedDateTime momentoUltimaTempRegistrada;

  public Heladera(String nombre,
                  Ubicacion ubicacion,
                  Colaborador encargado,
                  ZonedDateTime fechaInstalacion,
                  int capacidadEnViandas,
                  double temperaturaMinimaCelsius,
                  double temperaturaMaximaCelsius,
                  double temperaturaDeseadaCelsius) {
    this.nombre = nombre;
    this.ubicacion = ubicacion;
    this.encargado = encargado;
    this.fechaInstalacion = fechaInstalacion;
    this.capacidadEnViandas = capacidadEnViandas;
    this.temperaturaMinimaCelsius = temperaturaMinimaCelsius;
    this.temperaturaMaximaCelsius = temperaturaMaximaCelsius;
    this.temperaturaDeseadaCelsius = temperaturaDeseadaCelsius;
  }

  public void setUltimaTempRegistradaCelsius(double temperatura) {
    ultimaTempRegistradaCelsius = temperatura;
    momentoUltimaTempRegistrada = ZonedDateTime.now();
  }

  private boolean ultimaTemperaturaEsVieja() {
    ZonedDateTime haceCincoMinutos = ZonedDateTime.now().minusMinutes(5);
    return momentoUltimaTempRegistrada.isBefore(haceCincoMinutos);
  }

  private boolean temperaturaEstaDentroDeRangoDeModelo() {
    return ultimaTempRegistradaCelsius > temperaturaMaximaCelsius || ultimaTempRegistradaCelsius < temperaturaMinimaCelsius;
  }

  private boolean temperaturaEstaDentroDeMargenDeTolerancia() {
    return ultimaTempRegistradaCelsius < temperaturaDeseadaCelsius + 2 && ultimaTempRegistradaCelsius > temperaturaDeseadaCelsius - 2;
  }

  public EstadoDeFuncionamiento getEstado() {
    if (momentoUltimaTempRegistrada == null) {
      return EstadoDeFuncionamiento.EN_FALLA;
    } else if (ultimaTemperaturaEsVieja()) {
      return EstadoDeFuncionamiento.EN_FALLA;
    } else if (temperaturaEstaDentroDeRangoDeModelo()) {
      return EstadoDeFuncionamiento.DEFICIENTE;
    } else if (temperaturaEstaDentroDeMargenDeTolerancia()) {
      return EstadoDeFuncionamiento.REPOSANDO;
    } else {
      return EstadoDeFuncionamiento.ENFRIANDO;
    }
  }

  public int mesesActiva() {
    if (getEstado() == EstadoDeFuncionamiento.EN_FALLA) return 0;
    return (int) ChronoUnit.MONTHS.between(fechaInstalacion, ZonedDateTime.now());
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
