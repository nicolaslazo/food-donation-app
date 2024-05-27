package ar.edu.utn.frba.dds.models.entities.heladera;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Ubicacion;
import lombok.NonNull;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Heladera {
  private final float temperaturaMinimaCelsius;
  private final float temperaturaMaximaCelsius;
  private final int capacidadEnViandas;
  @NonNull
  private final ZonedDateTime fechaInstalacion;
  @NonNull
  private final List<Vianda> viandas;
  @NonNull
  private final Colaborador colaboradorACargo;
  @NonNull
  private String nombre;
  private Ubicacion ubicacion;
  private float temperaturaDeseadaCelsius;
  private float ultimaTempRegistradaCelsius;
  private ZonedDateTime momentoUltimaTempRegistrada;

  public Heladera(@NonNull String nombre, @NonNull Colaborador colaboradorACargo, int capacidadEnViandas, @NonNull ZonedDateTime fechaInstalacion, float temperaturaMinimaCelsius, float temperaturaMaximaCelsius, float temperaturaDeseadaCelsius) {
    this.temperaturaMinimaCelsius = temperaturaMinimaCelsius;
    this.temperaturaMaximaCelsius = temperaturaMaximaCelsius;
    this.nombre = nombre;
    this.capacidadEnViandas = capacidadEnViandas;
    this.fechaInstalacion = fechaInstalacion;
    this.temperaturaDeseadaCelsius = temperaturaDeseadaCelsius;
    this.colaboradorACargo = colaboradorACargo;

    this.viandas = new ArrayList<>();
  }

  public void setUltimaTempRegistradaCelsius(float temperatura) {
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
    // TODO: testear
    if (getEstado() == EstadoDeFuncionamiento.EN_FALLA) return 0;
    return (int) ChronoUnit.MONTHS.between(fechaInstalacion, ZonedDateTime.now());
  }
}
