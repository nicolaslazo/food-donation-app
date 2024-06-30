package ar.edu.utn.frba.dds.sensores;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.incidente.TipoIncidente;


public class ReceptorMovimiento{
    private AccionadorHeladera accionadorHeladera;

    public void evaluarReceptor(String datos, Heladera heladera) {
        accionadorHeladera.sucedeIncidente(TipoIncidente.FRAUDE,heladera);
    }
}
