package ar.edu.utn.frba.dds.domain;

import ar.edu.utn.frba.dds.domain.colaborador.Colaborador;
import ar.edu.utn.frba.dds.domain.ubicacion.Ubicacion;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Heladera {

  private String nombre;
  private Ubicacion ubicacion;
  @Getter
  private boolean enAlta;
  private int capacidadEnVianda;

  @Getter
  private ZonedDateTime fechaInstalacion;
  private List<Vianda> viandas;
  private float temperaturaMinima;
  private float temperaturaMaxima;
  private float ultimaTempRegistrada;
  private float temperaturaDeseada;
  private Colaborador colaboradorACargo;

  public Heladera() {
  }

  public Vianda sacarVianda() {
    // TODO implement here
    return null;
  }

  public void ingresarViandas(List<Vianda> viandas) {
    // TODO implement here
  }

  public void alertarMovimiento() {
    // TODO implement here
  }

  public int mesesActiva ()
  {
    // TODO: testear
    if (!isEnAlta()) return 0;  // TODO: Es lo mismo activa que en alta?
    return (int) ChronoUnit.MONTHS.between(fechaInstalacion, ZonedDateTime.now());
  }
}
