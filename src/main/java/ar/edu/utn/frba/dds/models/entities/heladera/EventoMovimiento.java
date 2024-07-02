package ar.edu.utn.frba.dds.models.entities.heladera;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.ZonedDateTime;

public final class EventoMovimiento {
  @Getter
  private final @NonNull Heladera heladera;

  @Getter
  private final @NonNull ZonedDateTime fecha;
  @Getter
  @Setter
  private int id;

  public EventoMovimiento(@NonNull Heladera heladera, @NonNull ZonedDateTime fecha) {
    this.heladera = heladera;
    this.fecha = fecha;
  }
}
