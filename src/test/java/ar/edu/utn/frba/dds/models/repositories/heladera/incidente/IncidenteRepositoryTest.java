package ar.edu.utn.frba.dds.models.repositories.heladera.incidente;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.Incidente;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.TipoIncidente;
import ar.edu.utn.frba.dds.models.repositories.heladera.incidente.IncidenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class IncidenteRepositoryTest {
    private IncidenteRepository incidenteRepository;
    private Incidente incidente;
    final Heladera heladeraMock = Mockito.mock(Heladera.class);
    final Colaborador colaboradorMock = Mockito.mock(Colaborador.class);
    @BeforeEach
    public void setUp() {
        incidenteRepository = new IncidenteRepository();
        incidente = new Incidente(
            heladeraMock,
            TipoIncidente.BAJA_TEMPERATURA,
            ZonedDateTime.now(),
                colaboradorMock,
                "",
                null
        );
    }

    @Test
    public void testInsertIncidenteHeladera() {
        incidenteRepository.insertIncidenteHeladera(incidente);
        assertEquals(1, incidenteRepository.getIncidenteHeladeras().size());
        assertEquals(1, incidente.getId());
    }

    @Test
    public void testGetIncidenteHeladera() {
        incidenteRepository.insertIncidenteHeladera(incidente);
        Optional<Incidente> foundIncidente = incidenteRepository.getIncidenteHeladera(1);
        assertTrue(foundIncidente.isPresent());
        assertEquals(incidente, foundIncidente.get());
    }

    @Test
    public void testDeleteIncidenteHeladera() {
        incidenteRepository.insertIncidenteHeladera(incidente);
        boolean isDeleted = incidenteRepository.deleteIncidenteHeladera(1);
        assertTrue(isDeleted);
        assertEquals(0, incidenteRepository.getIncidenteHeladeras().size());
    }

    @Test
    public void testExistsIncidenteHeladera() {
        incidenteRepository.insertIncidenteHeladera(incidente);
        assertTrue(incidenteRepository.existsIncidenteHeladera(1));
    }

    @Test
    public void testDeleteTodos() {
        incidenteRepository.insertIncidenteHeladera(incidente);
        incidenteRepository.deleteTodos();
        assertEquals(0, incidenteRepository.getIncidenteHeladeras().size());
    }
}

