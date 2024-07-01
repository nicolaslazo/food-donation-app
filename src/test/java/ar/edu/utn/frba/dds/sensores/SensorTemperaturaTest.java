package ar.edu.utn.frba.dds.sensores;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import ar.edu.utn.frba.dds.sensores.comandos.ActualizarRepositoryHeladera;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SensorTemperaturaTest {
    final Ubicacion obelisco = new Ubicacion(-34.5611745, -58.4287506);
    final Colaborador colaboradorMock = Mockito.mock(Colaborador.class);
    final ReceptorTemperatura receptorTemperaturaMock = Mockito.mock(ReceptorTemperatura.class);
    final Heladera heladera = new Heladera(
            "Heladera Test",
            null,
            Mockito.mock(Colaborador.class),
            10,
            ZonedDateTime.now().minusMonths(6)
    );
    HeladerasRepository heladerasRepository;
    final AccionadorHeladera accionadorHeladera = new AccionadorHeladera();

    @BeforeEach
    void setUp() throws RepositoryException {
        heladerasRepository = HeladerasRepository.getInstancia();

        heladerasRepository.insert(heladera);

        accionadorHeladera.agregarComando(
                new ActualizarRepositoryHeladera()
        );
    }
    //TODO Estoy bien encaminado?
    @Test
    void recibirDatosCorrectos() throws MqttException {
        SensorTemperatura sensor = new SensorTemperatura(
                receptorTemperaturaMock,
                heladera
        );
        sensor.recibirDatos(0.0);
        assertEquals(0.0, heladera.getUltimaTempRegistradaCelsius());
    }

}
