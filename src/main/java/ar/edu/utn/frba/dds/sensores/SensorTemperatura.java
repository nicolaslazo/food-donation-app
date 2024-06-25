package ar.edu.utn.frba.dds.sensores;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class SensorTemperatura implements IMqttMessageListener {
    private ReceptorTemperatura receptorTemperatura;
    private Heladera heladera;

    //TODO Como vamos a recibir los datos para parsearlos? lo dejo asi por ahora
    public void recibirDatos(String datos) {
        this.receptorTemperatura.evaluarReceptor(
                this.obtenerTemperatura(datos),
                this.heladera
        );
    }

    public double obtenerTemperatura(String datos) {
        //TODO obtengo del String recibido del MQTT la temperatura
        return 0.0;
    }


    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        this.recibirDatos(topic);
    }
}
