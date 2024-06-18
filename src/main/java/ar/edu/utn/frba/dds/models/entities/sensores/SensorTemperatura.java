package ar.edu.utn.frba.dds.models.entities.sensores;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;

public class SensorTemperatura {
    private ReceptorTemperatura receptorTemperatura;
    private Heladera heladera; //TODO tengo mis dudas de esto, espero la respuesta en DS

    public void evaluarTemperaturaRecibida(Double temperatura) {
        //TODO dudas y mas dudas, no me respondieron en el DS
        receptorTemperatura.evaluarReceptor(temperatura,heladera);
    }

    public void recibirDatos(String datos) {
        //TODO Recibirias los datos como MQTT del Broker
    }
}
