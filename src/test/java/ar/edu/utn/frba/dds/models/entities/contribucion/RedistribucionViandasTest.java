package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

class RedistribucionViandasTest {
  @Test
  void testSetearFechaIniciadaActualizaUbicacionDeViandas() throws Exception {
    ZonedDateTime ahora = ZonedDateTime.now();
    Vianda vianda = new Vianda("", ahora, ahora, mock(Colaborador.class), 1, 1);
    vianda.setHeladera(mock(Heladera.class));

    RedistribucionViandas redistribucion = new RedistribucionViandas(mock(Colaborador.class),
        List.of(vianda),
        mock(Heladera.class),
        mock(Heladera.class),
        mock(MotivoDeDistribucion.class));

    redistribucion.setFechaIniciada(ahora);

    assertNull(vianda.getHeladera());
  }
}