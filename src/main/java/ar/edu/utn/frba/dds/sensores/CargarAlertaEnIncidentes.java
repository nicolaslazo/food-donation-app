package ar.edu.utn.frba.dds.sensores;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.incidente.IncidenteHeladera;
import ar.edu.utn.frba.dds.models.entities.incidente.TipoIncidenteHeladera;
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

    public void cargarIncidente(TipoAlertaHeladera tipoAlertaHeladera, Heladera heladera) {
        incidenteRepository.insertIncidenteHeladera(
                new IncidenteHeladera(
                        incidenteRepository.getSize() + 1,
                        ZonedDateTime.now(),
                        tipoAlertaHeladera,
                        TipoIncidenteHeladera.ALERTA,
                        null,
                        heladera,
                        "Se detecto una alerta en la heladera de tipo: " +
                                tipoAlertaHeladera.toString(),
                        null
                )
        );
    }
}
