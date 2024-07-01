package ar.edu.utn.frba.dds.sensores;

import ar.edu.utn.frba.dds.dtos.inputs.sensores.SensorTemperaturaInputDTO;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.*;


public class SensorTemperatura implements IMqttMessageListener {
    private ReceptorTemperatura receptorTemperatura;
    private Heladera heladera;
    private final static String BROKER_URL = "tcp://tu.broker.mqtt:1883";
    private final static String TOPIC = "heladera/temperatura";
    private MqttClient client;

    public SensorTemperatura(ReceptorTemperatura receptorTemperatura, Heladera heladera)
            throws MqttException {

        this.receptorTemperatura = receptorTemperatura;
        this.heladera = heladera;
        this.client = new MqttClient(BROKER_URL, MqttClient.generateClientId());
        connectAndSubscribe();
    }

    private void connectAndSubscribe() {
        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            client.connect(options);
            client.subscribe(TOPIC, this);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    //TODO Como vamos a recibir los datos para parsearlos? lo dejo asi por ahora
    public void recibirDatos(double temperatura) {
        this.receptorTemperatura.evaluarReceptor(
                temperatura,
                this.heladera
        );
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Gson gson = new Gson();
        SensorTemperaturaInputDTO sensorInput = gson.fromJson(
                new String(message.getPayload()), SensorTemperaturaInputDTO.class
        );
        this.recibirDatos(sensorInput.getTemperatura());
    }
}
