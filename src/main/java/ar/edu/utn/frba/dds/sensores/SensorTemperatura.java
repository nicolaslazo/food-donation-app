package ar.edu.utn.frba.dds.sensores;

import ar.edu.utn.frba.dds.dtos.inputs.sensores.SensorInputDTO;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class SensorTemperatura implements IMqttMessageListener {
    private ReceptorTemperatura receptorTemperatura;
    private Heladera heladera;

    public SensorTemperatura(ReceptorTemperatura receptorTemperatura, Heladera heladera) {
        this.receptorTemperatura = receptorTemperatura;
        this.heladera = heladera;
    }

    //TODO Como vamos a recibir los datos para parsearlos? lo dejo asi por ahora
    public void recibirDatos(double temperatura) {
        this.receptorTemperatura.evaluarReceptor(
                temperatura,
                this.heladera
        );
    }

    @Override
    public void messageArrived(String topic, MqttMessage payload) throws Exception {
        SensorInputDTO mensaje = SensorInputDTO.desdeJson(payload.toString());
        this.recibirDatos(
                mensaje.getTemperatura()
        );
    }
}
