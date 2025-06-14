package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.recompensas.Recompensa;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "OfertaRecompensa")
public class OfertaRecompensa extends Contribucion {

  @ManyToOne
  @JoinColumn(name = "idRecompensa", referencedColumnName = "id", nullable = false)
  @NonNull Recompensa recompensa;

  public OfertaRecompensa(@NonNull Colaborador colaborador, @NonNull Recompensa recompensa) {
    super(colaborador);
    this.recompensa = recompensa;
  }

  protected OfertaRecompensa() {
  }
}
