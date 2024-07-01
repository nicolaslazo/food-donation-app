package ar.edu.utn.frba.dds.sensores;

import ar.edu.utn.frba.dds.dtos.inputs.sensores.SensorInputDTO;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class SensorMovimiento implements IMqttMessageListener {
    private ReceptorMovimiento receptorMovimiento;
    private Heladera heladera;

    //TODO como parseariamos los datos? que es lo que importa?
    public void recibirDatos(String datos) {
        receptorMovimiento.evaluarReceptor(datos,heladera);
    }

    @Override
    public void messageArrived(String topic, MqttMessage payload) throws Exception {
        SensorInputDTO mensaje = SensorInputDTO.desdeJson(payload.toString());
        //TODO primero quiero implementar el de Temperatura
        this.recibirDatos(payload.toString());
    }

}
