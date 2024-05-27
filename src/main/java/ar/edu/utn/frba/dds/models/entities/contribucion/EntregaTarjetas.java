package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.PersonaVulnerable;
import ar.edu.utn.frba.dds.models.entities.TarjetaAlimentaria;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.repositories.PersonaVulnerableRepository;
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
  private List<TarjetaAlimentaria> tarjetasRepartidas;


  private String idTarjeta;

  public EntregaTarjetas(@NonNull Colaborador colaborador, @NonNull ZonedDateTime fecha,
                         @NonNull List<TarjetaAlimentaria> tarjetaAlimentarias) {
    super(colaborador, fecha);
    this.colaborador = colaborador;
    this.fecha = fecha;
    this.tarjetasRepartidas = tarjetaAlimentarias;
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
