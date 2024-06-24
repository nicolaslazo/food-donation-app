package ar.edu.utn.frba.dds.sensores;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;

public class SensorMovimiento {
    private ReceptorMovimiento receptorMovimiento;
    private Heladera heladera;

    //TODO como parseariamos los datos? que es lo que importa?
    public void recibirDatos(String datos) {
        //TODO recibo los datos por MQTT del sensor fisico
        receptorMovimiento.evaluarReceptor(datos,heladera);
    }

}
