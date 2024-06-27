package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

public class EntregaTarjetas extends Contribucion {
  private final @NonNull Colaborador colaborador;
  private final @NonNull ZonedDateTime fecha;
  @Getter
  private final List<Tarjeta> tarjetasRepartidas;
  @Getter
  @Setter
  private int id;

  public EntregaTarjetas(@NonNull Colaborador colaborador, @NonNull ZonedDateTime fecha,
                         @NonNull List<Tarjeta> tarjetas) {
    super(colaborador, fecha);
    this.colaborador = colaborador;
    this.fecha = fecha;
    this.tarjetasRepartidas = tarjetas;
  }

  @Override
  public String toString() {
    return "EntregaTarjetas{" +
        "colaborador=" + colaborador +
        ", fecha=" + fecha +
        '}';
  }

  public int getNumeroTarjetas() {
    return tarjetasRepartidas.size();
  }
}
