package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.config.ConfigLoader;
import lombok.Getter;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import javax.net.ssl.SSLSocketFactory;

public class MqttBrokerService {
  private static MqttBrokerService instancia = null;
  @Getter
  private final MqttClient mqttClient;

  public MqttBrokerService() throws MqttException {
    ConfigLoader configLoader = ConfigLoader.getInstancia();

    mqttClient = new MqttClient(configLoader.getProperty("mqtt.broker.cluster.url"),
        MqttClient.generateClientId(),
        new MemoryPersistence());

    MqttConnectOptions opcionesMqttClient = new MqttConnectOptions();
    opcionesMqttClient.setUserName(configLoader.getProperty("mqtt.broker.cluster.username"));
    opcionesMqttClient.setPassword(configLoader.getProperty("mqtt.broker.cluster.password").toCharArray());
    opcionesMqttClient.setSocketFactory(SSLSocketFactory.getDefault());
    opcionesMqttClient.setCleanSession(true);

    mqttClient.connect(opcionesMqttClient);
  }

  public static MqttBrokerService getInstancia() throws MqttException {
    if (instancia == null) {
      instancia = new MqttBrokerService();
    }

    return instancia;
  }

  public static void main(String[] args) throws MqttException {
    class DemoReceptor implements IMqttMessageListener {
      @Override
      public void messageArrived(String topic, MqttMessage message) {
        System.out.println("Topic: " + topic + ", mensaje: " + message.toString());
      }
    }
    DemoReceptor receptor = new DemoReceptor();

    MqttBrokerService.getInstancia().suscribir("demo", receptor);
  }

  public void suscribir(String topic, IMqttMessageListener receptor) {
    try {
      mqttClient.subscribe(topic, receptor);
    } catch (MqttException e) {
      throw new RuntimeException(e);
    }
  }
}
