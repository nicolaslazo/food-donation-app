package ar.edu.utn.frba.dds.sensores;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;



public class ReceptorMovimiento{
    private AccionadorHeladera accionadorHeladera;

    public void evaluarReceptor(String datos, Heladera heladera) {
        accionadorHeladera.sucedeIncidente(TipoAlertaHeladera.FRAUDE,heladera);
    }
}
