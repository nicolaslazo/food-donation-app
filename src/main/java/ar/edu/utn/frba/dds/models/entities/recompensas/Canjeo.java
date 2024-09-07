package ar.edu.utn.frba.dds.models.entities.recompensas;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import lombok.Getter;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(name = "canjeo")
public class Canjeo {
  @Id
  @GeneratedValue
  @Column(name = "id", unique = true, nullable = false, updatable = false)
  Long id;

  @ManyToOne(targetEntity = Colaborador.class)
  @JoinColumn(name = "idColaborador", referencedColumnName = "idUsuario", nullable = false, updatable = false)
  @Getter
  @NonNull Colaborador colaborador;

  @ManyToOne(targetEntity = Recompensa.class)
  @JoinColumn(name = "idRecompensa", referencedColumnName = "id", nullable = false, updatable = false)
  @Getter
  @NonNull Recompensa recompensa;

  @Column(name = "fecha", nullable = false, updatable = false)
  @NonNull ZonedDateTime fecha;

  public Canjeo(Colaborador colaborador, Recompensa recompensa, ZonedDateTime fecha) {
    this.colaborador = colaborador;
    this.recompensa = recompensa;
    this.fecha = fecha;
  }

  protected Canjeo() {
  }
}