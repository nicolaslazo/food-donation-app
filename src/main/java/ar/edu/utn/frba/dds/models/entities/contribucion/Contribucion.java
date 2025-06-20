package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.InheritanceType;
import javax.persistence.GenerationType;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import java.time.ZonedDateTime;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
public abstract class Contribucion {
  @Setter
  @Getter
  @Column(name = "id", nullable = false, updatable = false, unique = true)
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Id
  Long id;

  @ManyToOne(targetEntity = Colaborador.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "idColaborador", referencedColumnName = "idUsuario")
  @NonNull Colaborador colaborador;

  @Column(name = "fechaContribucionRealizada")
  ZonedDateTime fechaRealizada = null;

  public Contribucion(@NonNull Colaborador colaborador) {
    this.colaborador = colaborador;
  }

  protected Contribucion() {
  }

  public void setFechaRealizada(@NonNull ZonedDateTime fechaRealizada) throws ContribucionYaRealizadaException {
    if (this.fechaRealizada != null)
      throw new ContribucionYaRealizadaException("Esta contribución ya fue realizada en" + this.fechaRealizada);

    this.fechaRealizada = fechaRealizada;
  }

  public boolean isConcluida() {
    return fechaRealizada != null;
  }

  @Override
  public String toString() {
    return "Contribucion{" +
        "colaborador=" + colaborador +
        ", fecha=" + fechaRealizada +
        '}';
  }
}