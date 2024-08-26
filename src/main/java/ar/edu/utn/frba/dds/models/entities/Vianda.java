package ar.edu.utn.frba.dds.models.entities;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name="Vianda")
public class Vianda {
  @Getter
  @Setter
  @Id
  @GeneratedValue
  Long id;

  @Column(name = "Descripcion", nullable = false, columnDefinition = "TEXT")
  @NonNull String descripcion;

  @Column(name = "FechaCaducidad", nullable = false, columnDefinition = "DATETIME")
  @NonNull ZonedDateTime fechaCaducidad;

  @Column(name = "FechaDonacion", nullable = false, columnDefinition = "DATETIME")
  @NonNull ZonedDateTime fechaDonacion;

  @ManyToMany
  // La tabla intermedia capaz seria la misma contribucion de DonacionVianda
  // Lo dejo asi, pq espero a la proxima clase para ver como se haria :)
  @NonNull Colaborador colaborador;

  @Getter
  @Column(name = "PesoEnGramos")
  Double pesoEnGramos;

  @Column(name = "CaloriasTotales")
  Integer caloriasVianda;

  @ManyToMany
  // Idem
  @Getter
  @Setter
  Heladera heladera;

  public Vianda(@NonNull String descripcion,
                @NonNull ZonedDateTime fechaCaducidad,
                @NonNull ZonedDateTime fechaDonacion,
                @NonNull Colaborador colaborador,
                Double pesoEnGramos,
                Integer caloriasVianda) {
    this.descripcion = descripcion;
    this.fechaCaducidad = fechaCaducidad;
    this.fechaDonacion = fechaDonacion;
    this.colaborador = colaborador;
    this.pesoEnGramos = pesoEnGramos;
    this.caloriasVianda = caloriasVianda;
  }

  protected Vianda() {}
}
