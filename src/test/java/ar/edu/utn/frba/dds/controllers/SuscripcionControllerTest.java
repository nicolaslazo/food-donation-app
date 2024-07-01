package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.controllers.heladera.incidente.IncidenteController;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Suscripcion;
import ar.edu.utn.frba.dds.models.entities.contribucion.MotivoDeDistribucion;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.TipoIncidente;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
import ar.edu.utn.frba.dds.models.repositories.contacto.SuscripcionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SuscripcionControllerTest {
  final Ubicacion obelisco = new Ubicacion(-34.5609872, -58.501046);
  final Ubicacion bibliotecaNacional = new Ubicacion(-34.5844291, -58.4164616);
  final Ubicacion centroCivicoBariloche = new Ubicacion(-41.133496, -71.3127926);
  final Heladera heladeraMock = mock(Heladera.class);
  final Colaborador colaboradorMock = mock(Colaborador.class);

  @BeforeEach
  void setUp() {
    when(heladeraMock.getUbicacion()).thenReturn(obelisco);
  }

  @Test
  void testCrearSuscripcion() throws RepositoryException {
    when(colaboradorMock.getUbicacion()).thenReturn(bibliotecaNacional);

    SuscripcionController
        .suscribirAHeladera(heladeraMock, MotivoDeDistribucion.FALLA_HELADERA, null, colaboradorMock);

    Optional<Suscripcion> suscripcionOpcional = SuscripcionRepository
        .getInstancia()
        .get(heladeraMock, MotivoDeDistribucion.FALLA_HELADERA, colaboradorMock);

    assertTrue(suscripcionOpcional.isPresent());

    Suscripcion encontrada = suscripcionOpcional.get();

    assertAll(
        () -> assertEquals(1, encontrada.getId()),
        () -> assertEquals(heladeraMock, encontrada.getHeladera()),
        () -> assertEquals(colaboradorMock, encontrada.getColaborador())
    );
  }

  @Test
  void testCrearSuscripcionFallaSiElUsuarioViveLejos() {
    when(colaboradorMock.getUbicacion()).thenReturn(centroCivicoBariloche);

    assertThrows(RuntimeException.class,
        () -> SuscripcionController.suscribirAHeladera(heladeraMock,
            MotivoDeDistribucion.FALLA_HELADERA,
            null,
            colaboradorMock));
  }

  @Test
  void testColaboradoresSonNotificadosDeIncidentes() {
    Colaborador colaboradorMock = mock(Colaborador.class);
    final List<Heladera> heladeras = new ArrayList<>(3);

    for (int i = 0; i < 3; i++) {
      heladeras.add(new Heladera("Heladera " + (i + 1),
          new Ubicacion(-34, -58 - i),
          colaboradorMock,
          10,
          ZonedDateTime.now()));
    }


    IncidenteController.getInstancia().crearAlerta(heladeras.get(0), TipoIncidente.FALLA_CONEXION, ZonedDateTime.now());

    ArgumentCaptor<String> capturador = ArgumentCaptor.forClass(String.class);
    verify(colaboradorMock).enviarMensaje(capturador.capture());

    final String mensajeGenerado = capturador.getValue();

    assertTrue(mensajeGenerado.contains("Se detectó una falla en la heladera Heladera 1"));
    assertFalse(mensajeGenerado.contains("* Heladera 1"));
    assertTrue(mensajeGenerado.contains("* Heladera 2"));
    assertTrue(mensajeGenerado.contains("* Heladera 3"));
  }
}