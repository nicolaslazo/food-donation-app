package ar.edu.utn.frba.dds.models.entities.incidente;

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
    @Getter
    @Setter
    int idIncidente;
    @NonNull
    Heladera heladera;
    @NonNull TipoIncidente tipo;
    @NonNull ZonedDateTime fecha;
    Colaborador colaborador;
    String descripcion;
    URL imagen;

    public Incidente(@NonNull Heladera heladera, @NonNull TipoIncidente tipo, @NonNull ZonedDateTime fecha) {
        this.heladera = heladera;
        this.tipo = tipo;
        this.fecha = fecha;
    }
}
