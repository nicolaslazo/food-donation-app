package ar.edu.utn.frba.dds.sensores;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.TipoIncidente;

import java.time.ZonedDateTime;

public class ReceptorTemperatura {
  double temperaturaMinima;
  double temperaturaMaxima;
  AccionadorHeladera accionador;

  public void evaluarReceptor(double temperaturaRecibida, Heladera heladera, ZonedDateTime momentoEvento) {
    if (temperaturaRecibida < temperaturaMinima) {
      heladera.setUltimaTempRegistradaCelsius(temperaturaRecibida);
      accionador.sucedeIncidente(TipoIncidente.BAJA_TEMPERATURA, heladera, momentoEvento);
    } else if (temperaturaRecibida > temperaturaMaxima) {
      heladera.setUltimaTempRegistradaCelsius(temperaturaRecibida);
      accionador.sucedeIncidente(TipoIncidente.BAJA_TEMPERATURA, heladera, momentoEvento);
    }
  }
}
