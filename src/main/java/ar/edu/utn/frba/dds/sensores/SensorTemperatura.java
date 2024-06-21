package ar.edu.utn.frba.dds.sensores;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;

public class SensorTemperatura {
    private ReceptorTemperatura receptorTemperatura;
    private Heladera heladera; //TODO tengo mis dudas de esto, espero la respuesta en DS

    public void recibirDatos(String datos) {
        //TODO Recibirias los datos como MQTT del Broker
        this.receptorTemperatura.evaluarReceptor(
                this.obtenerTemperatura(datos),
                this.heladera
        );
    }

    public double obtenerTemperatura(String datos) {
        //TODO obtengo del String recibido del MQTT la temperatura
        return 0.0;
    }
}
