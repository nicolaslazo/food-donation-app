package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

public class DonacionViandas extends Contribucion {
  @NonNull
  private final Colaborador colaborador;
  @NonNull
  private final ZonedDateTime fecha;
  @NonNull
  @Getter
  private final List<Vianda> viandas;
  @Getter
  @Setter
  private int id;

  public DonacionViandas(@NonNull Colaborador colaborador, @NonNull ZonedDateTime fecha, @NonNull List<Vianda> viandas) {
    super(colaborador, fecha);
    this.colaborador = colaborador;
    this.fecha = fecha;
    this.viandas = viandas;
  }

  public int getNumeroViandas() {
    return viandas.size();
  }

  @Override
  public String toString() {
    return "DonacionViandas{" +
        "colaborador=" + colaborador +
        ", fecha=" + fecha +
        ", cantidadViandas=" + viandas.size() +
        '}';
  }
}
