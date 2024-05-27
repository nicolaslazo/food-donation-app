package ar.edu.utn.frba.dds.models.entities.heladera;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Ubicacion;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Heladera {
  private final float temperaturaMinimaCelsius;
  private final float temperaturaMaximaCelsius;
  private String nombre;
  private Ubicacion ubicacion;
  private int capacidadEnViandas;
  private ZonedDateTime fechaInstalacion;
  private List<Vianda> viandas;
  private float temperaturaDeseadaCelsius;
  private float ultimaTempRegistradaCelsius;
  private ZonedDateTime momentoUltimaTempRegistrada;
  private Colaborador colaboradorACargo;

  public Heladera(float temperaturaMinimaCelsius, float temperaturaMaximaCelsius, float temperaturaDeseadaCelsius) {
    this.temperaturaMinimaCelsius = temperaturaMinimaCelsius;
    this.temperaturaMaximaCelsius = temperaturaMaximaCelsius;
    this.temperaturaDeseadaCelsius = temperaturaDeseadaCelsius;
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
    if (ultimaTemperaturaEsVieja()) {
      return EstadoDeFuncionamiento.EN_FALLA;
    } else if (temperaturaEstaDentroDeRangoDeModelo()) {
      return EstadoDeFuncionamiento.DEFICIENTE;
    } else if (temperaturaEstaDentroDeMargenDeTolerancia()) {
      return EstadoDeFuncionamiento.REPOSANDO;
    } else {
      return EstadoDeFuncionamiento.ENFRIANDO;
    }
  }

  public Vianda sacarVianda() {
    // TODO implement here
    return null;
  }

  public void ingresarViandas(List<Vianda> viandas) {
    // TODO implement here
  }

  public int mesesActiva() {
    // TODO: testear
    if (getEstado() == EstadoDeFuncionamiento.EN_FALLA) return 0;
    return (int) ChronoUnit.MONTHS.between(fechaInstalacion, ZonedDateTime.now());
  }
}
