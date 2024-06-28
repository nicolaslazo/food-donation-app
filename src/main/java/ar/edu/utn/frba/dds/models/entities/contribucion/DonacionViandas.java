package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
public class DonacionViandas extends Contribucion {
  final @NonNull List<Vianda> viandas;
  @Setter
  private int id;

  public DonacionViandas(@NonNull Colaborador colaborador, @NonNull List<Vianda> viandas) {
    super(colaborador);
    this.viandas = viandas;
  }

  public int getNumeroViandas() {
    return viandas.size();
  }

  @Override
  public String toString() {
    return "DonacionViandas{" +
        "colaborador=" + colaborador +
        ", fechaRealizada=" + fechaRealizada +
        ", cantidadViandas=" + viandas.size() +
        '}';
  }
}
