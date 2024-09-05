package ar.edu.utn.frba.dds.sensores;

import ar.edu.utn.frba.dds.controllers.heladera.HeladeraController;
import ar.edu.utn.frba.dds.controllers.heladera.incidente.IncidenteController;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.TipoIncidente;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;

import java.time.ZonedDateTime;

public class AccionadorHeladera {
  public IncidenteController instanciaIncidenteController = IncidenteController.getInstancia();

  private void registrarIncidente(TipoIncidente tipoIncidente, Heladera heladera, ZonedDateTime momentoEvento) {
    instanciaIncidenteController.crearAlerta(heladera, tipoIncidente, momentoEvento);
  }

  //Es el metodo que se llama a la hora de detectar algun tipo de Incidente
  public void sucedeIncidente(TipoIncidente tipoIncidente, Heladera heladera, ZonedDateTime momentoEvento) {
    registrarIncidente(tipoIncidente, heladera, momentoEvento);
    registrarTemperatura(heladera);
  }

  public void registrarTemperatura(Heladera heladera) {
    new HeladerasRepository().updateTiempoHeladera(heladera.getId(), heladera.getUltimaTempRegistradaCelsius());
  }

}
