package ar.edu.utn.frba.dds.models.entities.heladera.incidente;

import ar.edu.utn.frba.dds.models.entities.Tecnico;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.net.URL;
import java.time.ZonedDateTime;

public class VisitaTecnica {
  final @NonNull Tecnico tecnico;
  @Getter
  final @NonNull Incidente incidente;
  final @NonNull ZonedDateTime fecha;
  @Getter
  final boolean incidenteResuelto;
  final String descripcion;
  final URL imagen;
  @Getter
  @Setter
  int id;

  public VisitaTecnica(@NonNull Tecnico tecnico,
                       @NonNull Incidente incidente,
                       @NonNull ZonedDateTime fecha,
                       boolean incidenteResuelto,
                       String descripcion,
                       URL imagen) {
    this.tecnico = tecnico;
    this.incidente = incidente;
    this.fecha = fecha;
    this.incidenteResuelto = incidenteResuelto;
    this.descripcion = descripcion;
    this.imagen = imagen;
  }

  public VisitaTecnica(@NonNull Tecnico tecnico,
                       @NonNull Incidente incidente,
                       @NonNull ZonedDateTime fecha,
                       boolean incidenteResuelto) {
    this(tecnico, incidente, fecha, incidenteResuelto, null, null);
  }
}
