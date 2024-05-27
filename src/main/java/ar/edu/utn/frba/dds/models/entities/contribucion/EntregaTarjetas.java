package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.TarjetaAlimentaria;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import lombok.Getter;
import lombok.NonNull;

import java.time.ZonedDateTime;
import java.util.List;

public class EntregaTarjetas extends Contribucion {
  @NonNull
  private final Colaborador colaborador;
  @NonNull
  private final ZonedDateTime fecha;
  @Getter
  private final List<TarjetaAlimentaria> tarjetas;

  public EntregaTarjetas(@NonNull Colaborador colaborador, @NonNull ZonedDateTime fecha, List<TarjetaAlimentaria> tarjetas) {
    super(colaborador, fecha);
    this.colaborador = colaborador;
    this.fecha = fecha;
    this.tarjetas = tarjetas;
  }

  public int getNumeroTarjetas() {
    return tarjetas.size();
  }
    
  @Override
  public String toString() {
    return "EntregaTarjetas{" +
        "colaborador=" + colaborador +
        ", fecha=" + fecha +
        ", cantidadTarjetas=" + tarjetas.size() +
        '}';
  }
}
