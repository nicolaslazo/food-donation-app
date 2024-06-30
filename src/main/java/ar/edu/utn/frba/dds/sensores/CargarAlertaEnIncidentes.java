package ar.edu.utn.frba.dds.sensores;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.incidente.Incidente;
import ar.edu.utn.frba.dds.models.entities.incidente.TipoIncidente;
import ar.edu.utn.frba.dds.models.repositories.incidenteHeladera.IIncidenteRepository;

import java.time.ZonedDateTime;

//Insertamos el nuevo incidente en el repositorio
public class CargarAlertaEnIncidentes {
    private static CargarAlertaEnIncidentes instancia = null;
    private IIncidenteRepository incidenteRepository;

    public CargarAlertaEnIncidentes getIntancia() {
        if(instancia == null) {
            return new CargarAlertaEnIncidentes();
        }
        return instancia;
    }

    public void cargarIncidente(TipoIncidente tipoIncidente, Heladera heladera) {
        incidenteRepository.insertIncidenteHeladera(
                new Incidente(
                        0,
                        heladera,
                        tipoIncidente,
                        ZonedDateTime.now(),
                        null,
                        "Se detecto una alerta en la heladera de tipo: " +
                                tipoIncidente.toString(),
                        null
                )
        );
    }
}
