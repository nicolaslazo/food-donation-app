package ar.edu.utn.frba.dds.models.entities.heladera;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Ubicacion;
import org.mockito.Mockito;

import java.time.ZonedDateTime;

class HeladeraTest {
  final Heladera heladera = new Heladera("Una heladera",
      Mockito.mock(Ubicacion.class),
      Mockito.mock(Colaborador.class),
      10,
      ZonedDateTime.now().minusMonths(6)
  );

}
