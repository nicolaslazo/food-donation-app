package ar.edu.utn.frba.dds.sensores;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.incidente.TipoIncidente;
import lombok.NonNull;

public class ReceptorTemperatura {
    @NonNull
    private double temperaturaMinima;
    @NonNull
    private double temperaturaMaxima;
    private AccionadorHeladera accionador;

    public void evaluarReceptor(double temperaturaRecibida, Heladera heladera) {
        if (temperaturaRecibida < temperaturaMinima) {
            heladera.setUltimaTempRegistradaCelsius(temperaturaRecibida);
            accionador.sucedeIncidente(TipoIncidente.BAJA_TEMPERATURA,heladera);
        }
        else if (temperaturaRecibida > temperaturaMaxima) {
            heladera.setUltimaTempRegistradaCelsius(temperaturaRecibida);
            accionador.sucedeIncidente(TipoIncidente.BAJA_TEMPERATURA,heladera);
        }
    }


}
