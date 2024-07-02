package ar.edu.utn.frba.dds.sensores;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.Incidente;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.TipoIncidente;
import ar.edu.utn.frba.dds.models.repositories.incidenteHeladera.IncidenteRepository;

import java.time.ZonedDateTime;

//Insertamos el nuevo incidente en el repositorio
public class CargarAlertaEnIncidentes {
    private static CargarAlertaEnIncidentes instancia = null;
    private final IncidenteRepository instanciaRepository = null;

    public CargarAlertaEnIncidentes getIntancia() {
        if(instancia == null) {
            return new CargarAlertaEnIncidentes();
        }
        return instancia;
    }

    public void cargarIncidente(TipoIncidente tipoIncidente, Heladera heladera) {
        instanciaRepository.getInstancia().insertIncidenteHeladera(
                new Incidente(
                        heladera,
                        tipoIncidente,
                        ZonedDateTime.now(),
                        null,
                        "Se detecto una alerta en la heladera de tipo: " +
                                tipoIncidente.toString(),
                        null,
                        0
                )
        );
    }
}
