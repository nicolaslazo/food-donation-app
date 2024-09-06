package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import lombok.Getter;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.CascadeType;

@Entity
@Table(name = "cuidadoHeladera", uniqueConstraints = {@UniqueConstraint(columnNames = "idHeladera")})
@Getter
public class CuidadoHeladera extends Contribucion {

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = Heladera.class)
  @JoinColumn(name = "idHeladera", referencedColumnName = "id", updatable = false, unique = true)
  Heladera heladera;

  public CuidadoHeladera(@NonNull Colaborador colaborador, Heladera heladera) {
    super(colaborador);
    this.heladera = heladera;
  }

  protected CuidadoHeladera() {}
}
