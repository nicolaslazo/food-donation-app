package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.DonacionViandas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DonacionViandasControllerTest {
  @Mock
  private Vianda vianda1;

  @Mock
  private Vianda vianda2;
  @Mock
  private Vianda vianda3;

  @Mock
  private Colaborador colaborador1;
  @Mock
  private Colaborador colaborador2;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testObtenerDonacionesPorColaboradorSemanaAnterior() {
    List<DonacionViandas> donacionesSemanaAnterior = new ArrayList<>();

    donacionesSemanaAnterior.add(new DonacionViandas(colaborador1, ZonedDateTime.now().minusDays(2),Arrays.asList(vianda1, vianda2)));
    donacionesSemanaAnterior.add(new DonacionViandas(colaborador2, ZonedDateTime.now().minusDays(3),Collections.singletonList(vianda3)));

    when(colaborador1.getApellido()).thenReturn("Perez");

    when(colaborador2.getApellido()).thenReturn("Lopez");

    when(colaborador1.getNombre()).thenReturn("Juan");

    when(colaborador2.getNombre()).thenReturn("Maria");

    Map<String, Integer> expectedDonaciones = new HashMap<>();
    expectedDonaciones.put("Juan Perez", 2);
    expectedDonaciones.put("Maria Lopez", 1);

    assertEquals(expectedDonaciones, DonacionViandasController.getInstance().MapDonacionesPorColaboradorSemanaAnterior(donacionesSemanaAnterior));
  }
}
