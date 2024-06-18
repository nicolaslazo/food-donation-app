package ar.edu.utn.frba.dds.models.entities.sensores;

public class ReceptorTemperatura {
    private Double temperaturaMinima;
    private Double temperaturaMaxima;
    //private Accionador accionador;

    public void evaluarReceptor(Double temperaturaRecibida) {
        if (temperaturaRecibida < temperaturaMinima) {
            //TODO
        }
        else if (temperaturaRecibida > temperaturaMaxima) {
            //TODO
        }
    }
}
