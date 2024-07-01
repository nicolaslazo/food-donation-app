package ar.edu.utn.frba.dds.sensores;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.TipoIncidente;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Ubicacion;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TemperatureSensorChecker {
    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        //TODO como hago esto? es correcto inicializar la heladera aca? obtengo la instancia de la heladera?
        // Ojo que rompe, lo deje asi para visualizar
        Heladera heladera = new Heladera("Heladera1", new Ubicacion(), new Colaborador(), 100, ZonedDateTime.now());
        ReceptorTemperatura receptorTemperatura = new ReceptorTemperatura();
        ReceptorMovimiento receptorMovimiento = new ReceptorMovimiento();
        CargarAlertaEnIncidentes cargadorIncidente = CargarAlertaEnIncidentes.getInstancia();

        try {
            SensorTemperatura sensorTemperatura = new SensorTemperatura(receptorTemperatura, heladera);
            SensorMovimiento sensorMovimiento = new SensorMovimiento(receptorMovimiento, heladera);

            // Programa la tarea para ejecutarse cada 5 minutos
            scheduler.scheduleAtFixedRate(() -> {
                ZonedDateTime now = ZonedDateTime.now();
                ZonedDateTime lastTemperatureUpdate = heladera.getMomentoUltimaTempRegistrada();

                if (lastTemperatureUpdate != null && ChronoUnit.MINUTES.between(lastTemperatureUpdate, now) > 5) {
                    cargadorIncidente.cargarIncidente(
                            TipoIncidente.FALLA_CONEXION,heladera
                    );
                }
                /* TODO
                    Como chequeariamos el sensor de movimiento?
                    Podriamos añadir un método similar en Heladera para el sensor de movimiento
                 */
            }, 0, 5, TimeUnit.MINUTES);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
