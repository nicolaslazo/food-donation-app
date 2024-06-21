package ar.edu.utn.frba.dds.models.entities.incidente;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.sensores.TipoAlertaHeladera;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.URL;
import java.time.ZonedDateTime;

@AllArgsConstructor
public class IncidenteHeladera {
    @Getter
    private int idIncidente;
    private ZonedDateTime fechaHora;
    private TipoAlertaHeladera tipoAlertaHeladera;
    private TipoIncidenteHeladera tipoIncidenteHeladera;
    private Colaborador colaborador;
    private Heladera heladera;
    private String descripcion;
    private URL imagen;
}
