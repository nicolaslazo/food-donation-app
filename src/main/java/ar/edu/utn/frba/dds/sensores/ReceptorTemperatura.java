package ar.edu.utn.frba.dds.sensores;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.TipoIncidente;

import java.time.ZonedDateTime;

public class ReceptorTemperatura {
  Double temperaturaMinima;
  Double temperaturaMaxima;
  AccionadorHeladera accionador;

  public ReceptorTemperatura(
          Double temperaturaMaxima,
          Double temperaturaMinima,
          AccionadorHeladera accionador) {
    this.temperaturaMaxima = temperaturaMaxima;
    this.temperaturaMinima = temperaturaMinima;
    this.accionador = accionador;
  }

  public void evaluarReceptor(Double temperaturaRecibida,
                              Heladera heladera,
                              ZonedDateTime momentoEvento) {
    if (temperaturaRecibida < temperaturaMinima) {
      heladera.setUltimaTempRegistradaCelsius(temperaturaRecibida);
      accionador.sucedeIncidente(TipoIncidente.BAJA_TEMPERATURA, heladera, momentoEvento);
    } else if (temperaturaRecibida > temperaturaMaxima) {
      heladera.setUltimaTempRegistradaCelsius(temperaturaRecibida);
      accionador.sucedeIncidente(TipoIncidente.BAJA_TEMPERATURA, heladera, momentoEvento);
    } else {
      heladera.setUltimaTempRegistradaCelsius(temperaturaRecibida);
      accionador.registrarTemperatura(heladera);
    }
  }
}
