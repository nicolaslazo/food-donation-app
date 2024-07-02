package ar.edu.utn.frba.dds.sensores;

import ar.edu.utn.frba.dds.dtos.inputs.sensores.SensorMovimientoInputDTO;
import ar.edu.utn.frba.dds.dtos.inputs.sensores.SensorTemperaturaInputDTO;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.services.MqttBrokerService;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.time.ZonedDateTime;

public class SensorMovimiento implements IMqttMessageListener {
    private ReceptorMovimiento receptorMovimiento;
    private Heladera heladera;

    public SensorMovimiento(ReceptorMovimiento receptorMovimiento, Heladera heladera) throws MqttException {
        this.receptorMovimiento = receptorMovimiento;
        this.heladera = heladera;
        MqttBrokerService.getInstancia().suscribir("heladera/movimiento", this);
    }

    public void recibirDatos(ZonedDateTime momentoEvento) {
        receptorMovimiento.evaluarReceptor(momentoEvento, heladera);
    }

    @Override
    public void messageArrived(String topic, MqttMessage payload) throws Exception {
        SensorMovimientoInputDTO mensaje = SensorMovimientoInputDTO.desdeJson(new String(payload.getPayload()));
        this.recibirDatos(mensaje.getTiempo());
    }
}
