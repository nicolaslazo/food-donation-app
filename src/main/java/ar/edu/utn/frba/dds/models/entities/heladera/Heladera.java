package ar.edu.utn.frba.dds.models.entities.heladera;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Ubicacion;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Heladera {
  private final int capacidadEnViandas;
  @NonNull
  private final ZonedDateTime fechaInstalacion;
  @NonNull
  private final List<Vianda> viandas;
  @Getter
  @NonNull
  private final Colaborador encargado;
  @Getter
  private final Ubicacion ubicacion;
  @Getter
  @Setter
  private int id;
  @NonNull
  private String nombre;
  @Getter
  private double ultimaTempRegistradaCelsius;
  private ZonedDateTime momentoUltimaTempRegistrada;
  @Getter
  @Setter
  private Boolean heladeraActiva;

  public Heladera(@NonNull String nombre,
                  Ubicacion ubicacion,
                  @NonNull Colaborador encargado,
                  int capacidadEnViandas,
                  @NonNull ZonedDateTime fechaInstalacion,
                  Boolean heladeraActiva) {
    this.nombre = nombre;
    this.ubicacion = ubicacion;
    this.encargado = encargado;
    this.capacidadEnViandas = capacidadEnViandas;
    this.fechaInstalacion = fechaInstalacion;
    this.heladeraActiva = heladeraActiva;

    this.viandas = new ArrayList<>();
  }

  public void setUltimaTempRegistradaCelsius(double temperatura) {
    ultimaTempRegistradaCelsius = temperatura;
    momentoUltimaTempRegistrada = ZonedDateTime.now();
  }

  public void setUltimaTempRegistradaCelsius() {
    momentoUltimaTempRegistrada = ZonedDateTime.now();
  }

  private boolean ultimaTemperaturaEsVieja() {
    ZonedDateTime haceCincoMinutos = ZonedDateTime.now().minusMinutes(5);
    return momentoUltimaTempRegistrada.isBefore(haceCincoMinutos);
  }

  public int mesesActiva() {
    if (!heladeraActiva) return 0;
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
