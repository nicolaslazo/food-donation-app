package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

@Getter
public class EntregaTarjetas extends Contribucion {
  final List<Tarjeta> tarjetasRepartidas;

  public EntregaTarjetas(@NonNull Colaborador colaborador, @NonNull List<Tarjeta> tarjetas) {
    super(colaborador);
    this.tarjetasRepartidas = tarjetas;
  }

  @Override
  public String toString() {
    return "EntregaTarjetas{" +
        "colaborador=" + colaborador +
        ", fechaRealizada=" + fechaRealizada +
        '}';
  }

  public int getNumeroTarjetas() {
    return tarjetasRepartidas.size();
  }
}
