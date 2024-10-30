package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import lombok.Getter;
import lombok.NonNull;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
public abstract class MovimientoViandas extends Contribucion {

  @OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
  @JoinColumn(name = "idVianda", referencedColumnName = "id")
  Collection<Vianda> viandas;

  @ManyToOne(targetEntity = Heladera.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST })
  @JoinColumn(name = "idHeladera", referencedColumnName = "id")
  Heladera destino;

  public MovimientoViandas(@NonNull Colaborador colaborador,
                           Collection<Vianda> viandas,
                           Heladera heladera) {
    super(colaborador);

    this.viandas = viandas;
    this.destino = heladera;
  }

  protected MovimientoViandas() {}

  public int getNumeroViandas() {
    return viandas.size();
  }

  public void setFechaRealizada(ZonedDateTime timestamp) {
    viandas
        .stream()
        .filter(Objects::nonNull)  // Caso especial por las contribuciones legacy
        .forEach(vianda -> vianda.setHeladera(destino));

    fechaRealizada = timestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MovimientoViandas that = (MovimientoViandas) o;
    return getId() == that.getId();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
