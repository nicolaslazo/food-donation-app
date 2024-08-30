package ar.edu.utn.frba.dds.models.entities;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name="vianda")
public class Vianda {
  @Getter
  @Setter
  @Id
  @Column(name = "id")
  @GeneratedValue
  Long id;

  @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
  @NonNull String descripcion;

  @Column(name = "fechaCaducidad", nullable = false, columnDefinition = "DATETIME")
  @NonNull ZonedDateTime fechaCaducidad;

  @Column(name = "fechaDonacion", nullable = false, columnDefinition = "DATETIME")
  @NonNull ZonedDateTime fechaDonacion;

  @ManyToOne
  @JoinColumn(name = "colaborador_id", referencedColumnName = "id")
  @NonNull Colaborador colaborador;

  @Getter
  @Column(name = "pesoEnGramos")
  Double pesoEnGramos;

  @Column(name = "caloriasTotales")
  Integer caloriasVianda;

  //TODO: ORMizar Heladera
  @ManyToOne
  @JoinColumn(name = "heladera_id", referencedColumnName = "id")
  @Getter
  @Setter
  @NonNull Heladera heladera;

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
