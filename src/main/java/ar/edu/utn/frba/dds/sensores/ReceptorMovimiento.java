package ar.edu.utn.frba.dds.sensores;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.TipoIncidente;

import java.time.ZonedDateTime;


public class ReceptorMovimiento {
  AccionadorHeladera accionadorHeladera;

  public void evaluarReceptor(ZonedDateTime momentoEvento, Heladera heladera) {
    accionadorHeladera.sucedeIncidente(TipoIncidente.FRAUDE, heladera, momentoEvento);
  }
}
