package ar.edu.utn.frba.dds.domain;

import ar.edu.utn.frba.dds.domain.colaborador.Colaborador;
import ar.edu.utn.frba.dds.domain.ubicacion.Ubicacion;

import java.time.ZonedDateTime;
import java.util.List;

public class Heladera {

  private String nombre;
  private Ubicacion ubicacion;
  private boolean enAlta;
  private int capacidadEnVianda;
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
}