package ar.edu.utn.frba.dds.models.entities.heladera.incidente;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.net.URL;
import java.time.ZonedDateTime;

public class Incidente {
  @Getter
  final @NonNull Heladera heladera;
  @Getter
  final @NonNull TipoIncidente tipo;
  @Getter
  final @NonNull ZonedDateTime fecha;
  final Colaborador colaborador;
  final String descripcion;
  final URL imagen;
  @Getter
  @Setter
  int id;

  public Incidente(@NonNull Heladera heladera, @NonNull TipoIncidente tipo, @NonNull ZonedDateTime fecha, Colaborador colaborador, String descripcion, URL imagen) {
    this.heladera = heladera;
    this.tipo = tipo;
    this.fecha = fecha;
    this.colaborador = colaborador;
    this.descripcion = descripcion;
    this.imagen = imagen;
  }

  public Incidente(@NonNull Heladera heladera, @NonNull TipoIncidente tipo, @NonNull ZonedDateTime fecha) {
    this(heladera, tipo, fecha, null, null, null);
  }
}
