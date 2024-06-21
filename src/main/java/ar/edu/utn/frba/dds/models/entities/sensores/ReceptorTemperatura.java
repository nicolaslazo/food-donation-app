package ar.edu.utn.frba.dds.models.entities.sensores;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import lombok.Builder;
import lombok.NonNull;
import lombok.Setter;

public class ReceptorTemperatura {
    @NonNull
    private double temperaturaMinima;
    @NonNull
    private double temperaturaMaxima;
    private AccionadorHeladera accionador;

    public void evaluarReceptor(Double temperaturaRecibida, Heladera heladera) {
        if (temperaturaRecibida < temperaturaMinima) {
            accionador.sucedeIncidente(TipoAlertaHeladera.BAJA_TEMPERATURA,heladera);
        }
        else if (temperaturaRecibida > temperaturaMaxima) {
            accionador.sucedeIncidente(TipoAlertaHeladera.BAJA_TEMPERATURA,heladera);
        }
    }
}
