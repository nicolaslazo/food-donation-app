package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class MovimientoViandasTest {
  @Test
  void testSetearFechaRealizadaActualizaUbicacionDeViandas() {
    Vianda viandaMock = mock(Vianda.class);
    Heladera heladeraMock = mock(Heladera.class);
    DonacionViandas movimientoViandas =
        new DonacionViandas(mock(Colaborador.class), List.of(viandaMock), heladeraMock);

    movimientoViandas.setFechaRealizada(ZonedDateTime.now());

    verify(viandaMock, times(1)).setHeladera(heladeraMock);
  }
}