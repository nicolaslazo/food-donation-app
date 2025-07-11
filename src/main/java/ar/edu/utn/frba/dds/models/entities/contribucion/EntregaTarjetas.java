package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import lombok.Getter;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "entregaTarjetas")
@Getter
public class EntregaTarjetas extends Contribucion {
  @OneToMany(targetEntity = Tarjeta.class, orphanRemoval = true)
  @JoinColumn(name = "idEntrega", referencedColumnName = "id")
  List<Tarjeta> tarjetasSolicitadas;

  public EntregaTarjetas(@NonNull Colaborador colaborador, @NonNull List<Tarjeta> tarjetas) {
    super(colaborador);
    this.tarjetasSolicitadas = tarjetas;
  }

  protected EntregaTarjetas() {}

  @Override
  public String toString() {
    return "EntregaTarjetas{" +
        "colaborador=" + colaborador +
        ", fechaRealizada=" + fechaRealizada +
        '}';
  }

  public int getNumeroTarjetas() {
    return tarjetasSolicitadas.size();
  }
}
