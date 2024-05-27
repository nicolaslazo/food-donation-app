package ar.edu.utn.frba.dds.models.entities;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;

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
