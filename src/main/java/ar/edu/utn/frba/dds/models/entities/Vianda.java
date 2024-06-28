package ar.edu.utn.frba.dds.models.entities;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.ZonedDateTime;

public class Vianda {
  final @NonNull String descripcion;
  final @NonNull ZonedDateTime fechaCaducidad;
  final @NonNull ZonedDateTime fechaDonacion;
  final @NonNull Colaborador colaborador;
  final double pesoViandaEnGramos;
  final int caloriasVianda;
  @Getter
  @Setter
  Heladera heladera;
  @Getter
  @Setter
  int id;

  public Vianda(@NonNull String descripcion,
                @NonNull ZonedDateTime fechaCaducidad,
                @NonNull ZonedDateTime fechaDonacion,
                @NonNull Colaborador colaborador,
                double pesoViandaEnGramos,
                int caloriasVianda) {
    this.descripcion = descripcion;
    this.fechaCaducidad = fechaCaducidad;
    this.fechaDonacion = fechaDonacion;
    this.colaborador = colaborador;
    this.pesoViandaEnGramos = pesoViandaEnGramos;
    this.caloriasVianda = caloriasVianda;
  }
}
