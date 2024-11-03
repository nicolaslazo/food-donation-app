package ar.edu.utn.frba.dds.models.entities;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(name = "vianda")
public class Vianda {
  @Id
  @Column(name = "id", nullable = false, unique = true, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(name = "descripcion", nullable = false, updatable = false)
  @NonNull String descripcion;

  @Column(name = "fechaCaducidad", nullable = false, updatable = false)
  @NonNull ZonedDateTime fechaCaducidad;

  @Column(name = "fechaDonacion", nullable = false, updatable = false)
  @NonNull ZonedDateTime fechaDonacion;

  @ManyToOne(targetEntity = Colaborador.class)
  @JoinColumn(name = "idColaborador", referencedColumnName = "idUsuario", nullable = false, updatable = false)
  @NonNull Colaborador colaborador;

  @Getter
  @Column(name = "pesoEnGramos", nullable = false, updatable = false)
  @NonNull Double pesoEnGramos;

  @Column(name = "caloriasTotales", nullable = false, updatable = false)
  @NonNull Integer caloriasVianda;

  @ManyToOne(targetEntity = Heladera.class)
  @JoinColumn(name = "idHeladera", referencedColumnName = "id", updatable = false)
  @Getter
  @Setter
  Heladera heladera;

  public Vianda(@NonNull String descripcion,
                @NonNull ZonedDateTime fechaCaducidad,
                @NonNull ZonedDateTime fechaDonacion,
                @NonNull Colaborador colaborador,
                @NonNull Double pesoEnGramos,
                @NonNull Integer caloriasVianda) {
    this.descripcion = descripcion;
    this.fechaCaducidad = fechaCaducidad;
    this.fechaDonacion = fechaDonacion;
    this.colaborador = colaborador;
    this.pesoEnGramos = pesoEnGramos;
    this.caloriasVianda = caloriasVianda;
  }


  protected Vianda() {
  }
}
