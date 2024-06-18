package ar.edu.utn.frba.dds.models.entities.sensores;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import lombok.Builder;
import lombok.Setter;

@Builder
public class ReceptorTemperatura {
    private Double temperaturaMinima;
    private Double temperaturaMaxima;
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
