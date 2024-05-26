package ar.edu.utn.frba.dds.domain;

import ar.edu.utn.frba.dds.domain.colaborador.Colaborador;
import ar.edu.utn.frba.dds.domain.heladera.Heladera;

import java.time.ZonedDateTime;

public class Vianda {
  private String descripcion;
  private ZonedDateTime fechaCaducidad;
  private ZonedDateTime fechaDonacion;
  private Colaborador colaborador;
  private float pesoViandaEnGramos;
  private int caloriasVianda;
  private boolean viandaAlmacenada;
  private Heladera heladera;

  public Vianda() {
  }

  public void contribuir() {
    // TODO implement here
  }
}
