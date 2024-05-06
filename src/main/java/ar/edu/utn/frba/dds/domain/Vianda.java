package ar.edu.utn.frba.dds.domain;

import ar.edu.utn.frba.dds.domain.colaborador.Colaborador;

public class Vianda implements Contribucion {

  private String descripcion;
  private LocalDateTime fechaCaducidad;
  private LocalDateTime fechaDonacion;
  private Colaborador colaborador;
  private float pesoViandaEnGramos;
  private int caloriasVianda;
  private boolean viandaAlmacenada;
  private Heladera heladera;

  public Vianda() {
  }

  public void contribuir() {
    // TODO implement here
    return null;
  }

}