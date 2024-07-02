package ar.edu.utn.frba.dds.sensores;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.TipoIncidente;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.incidenteHeladera.IncidenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.ArgumentCaptor;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TemperatureSensorCheckerTest {
    final Ubicacion obelisco = new Ubicacion(-34.5611745, -58.4287506);
    final Colaborador colaboradorMock = Mockito.mock(Colaborador.class);

    //Heladera en perfecto funcionamiento
    final Heladera heladera = new Heladera("Una heladera",
            obelisco,
            colaboradorMock,
            50,
            ZonedDateTime.now().minusMonths(5)
    );

    //Heladera defectuosa
    final Heladera otraHeladera = new Heladera("Otra heladera",
            new Ubicacion(-34, -58),
            colaboradorMock,
            60,
            ZonedDateTime.now().minusMonths(7)
    );
    HeladerasRepository repositoryHeladeras = HeladerasRepository.getInstancia();
    IncidenteRepository repositoryIncidentes = IncidenteRepository.getInstancia();
    CargarAlertaEnIncidentes cargadorIncidenteMock = Mockito.mock(CargarAlertaEnIncidentes.class);
    TemperatureSensorChecker temperatureSensorChecker = new TemperatureSensorChecker(cargadorIncidenteMock);
    
    
    @BeforeEach
    void setUp() throws RepositoryException {
        repositoryHeladeras.deleteTodas();

        //Esta heladera no falla, OtraHeladera si
        heladera.setUltimaTempRegistradaCelsius(0.0);

        //Esta heladera si falla
        otraHeladera.setMomentoUltimaTempRegistrada(ZonedDateTime.now().minusMinutes(7));

        repositoryHeladeras.insert(heladera);
        repositoryHeladeras.insert(otraHeladera);
    }

    @Test
    void testTemperatureSensorChecker() throws CheckerException {
        // Ejecutamos el método a probar
        TemperatureSensorChecker.main(null);

        // Verificamos que se haya llamado al método cargarIncidente para la heladera desactualizada
        ArgumentCaptor<TipoIncidente> tipoIncidenteCaptor = ArgumentCaptor.forClass(TipoIncidente.class);
        ArgumentCaptor<Heladera> heladeraCaptor = ArgumentCaptor.forClass(Heladera.class);

        verify(cargadorIncidenteMock).cargarIncidente(tipoIncidenteCaptor.capture(), heladeraCaptor.capture());

        assertEquals(TipoIncidente.FALLA_CONEXION, tipoIncidenteCaptor.getValue());
        assertEquals(otraHeladera, heladeraCaptor.getValue());
    }

    @Test
    void testTemperatureSensorCheckerThrowsException() throws CheckerException {
        repositoryHeladeras.deleteTodas();

        TemperatureSensorChecker temperatureSensorChecker = new TemperatureSensorChecker(cargadorIncidenteMock);

        CheckerException exception = assertThrows(CheckerException.class, () -> {
            temperatureSensorChecker.main(null);
        });
    }
}
