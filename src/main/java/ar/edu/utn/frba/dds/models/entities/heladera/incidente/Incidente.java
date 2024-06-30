package ar.edu.utn.frba.dds.models.entities.heladera.incidente;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.net.URL;
import java.time.ZonedDateTime;

@AllArgsConstructor
public class Incidente {
    final @NonNull Heladera heladera;
    final @NonNull TipoIncidente tipo;
    final @NonNull ZonedDateTime fecha;
    final Colaborador colaborador;
    final String descripcion;
    final URL imagen;
    @Getter
    @Setter
    int idIncidente;

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
