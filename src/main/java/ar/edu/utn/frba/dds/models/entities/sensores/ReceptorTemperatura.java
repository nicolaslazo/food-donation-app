package ar.edu.utn.frba.dds.models.entities.sensores;

public class ReceptorTemperatura {
    private Double temperaturaMinima;
    private Double temperaturaMaxima;
    private AccionadorHeladera accionador;

    public void evaluarReceptor(Double temperaturaRecibida) {
        if (temperaturaRecibida < temperaturaMinima) {
            accionador.sucedeIncidente(TipoAlertaHeladera.BAJA_TEMPERATURA);
        }
        else if (temperaturaRecibida > temperaturaMaxima) {
            accionador.sucedeIncidente(TipoAlertaHeladera.BAJA_TEMPERATURA);
        }
    }
}
