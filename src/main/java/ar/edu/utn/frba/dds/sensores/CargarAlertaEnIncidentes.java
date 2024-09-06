package ar.edu.utn.frba.dds.sensores;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.Incidente;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.TipoIncidente;
import ar.edu.utn.frba.dds.models.repositories.heladera.incidente.IncidenteRepository;

import java.time.ZonedDateTime;

//Insertamos el nuevo incidente en el repositorio
public class CargarAlertaEnIncidentes {
  public static void cargarIncidente(TipoIncidente tipoIncidente, Heladera heladera) {
    new IncidenteRepository().insert(
        new Incidente(
            heladera,
            tipoIncidente,
            ZonedDateTime.now(),
            null,
            "Se detecto una alerta en la heladera de tipo: " + tipoIncidente,
            null
        )
    );
  }
}
