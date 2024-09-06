package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import lombok.Getter;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cuidadoHeladera")
@Getter
public class CuidadoHeladera extends Contribucion {

  @ManyToOne
  @JoinColumn(name = "idHeladera", referencedColumnName = "id")
  Heladera heladera;

  public CuidadoHeladera(@NonNull Colaborador colaborador, Heladera heladera) {
    super(colaborador);
    this.heladera = heladera;
  }

  protected CuidadoHeladera() {}
}
