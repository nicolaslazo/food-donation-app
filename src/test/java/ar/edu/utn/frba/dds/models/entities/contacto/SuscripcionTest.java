package ar.edu.utn.frba.dds.models.entities.contacto;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SuscripcionTest {
  @Test
  void testGeneraNombreDeTopic() {
    final Heladera heladeraMock = Mockito.mock(Heladera.class);
    final Colaborador colaboradorMock = Mockito.mock(Colaborador.class);

    when(heladeraMock.getId()).thenReturn(42);

    assertEquals("heladera/42/FALLA_HELADERA",
        new Suscripcion(heladeraMock, TipoNotificacion.FALLA_HELADERA, colaboradorMock).getTopic());
  }
}