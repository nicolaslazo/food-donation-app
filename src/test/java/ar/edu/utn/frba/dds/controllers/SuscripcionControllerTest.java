package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.controllers.heladera.incidente.IncidenteController;
import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Suscripcion;
import ar.edu.utn.frba.dds.models.entities.contribucion.MotivoDeDistribucion;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudAperturaPorContribucion;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudInvalidaException;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.TipoIncidente;
import ar.edu.utn.frba.dds.models.entities.ubicacion.Ubicacion;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
import ar.edu.utn.frba.dds.models.repositories.ViandasRepository;
import ar.edu.utn.frba.dds.models.repositories.contacto.SuscripcionRepository;

import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.SolicitudAperturaPorContribucionRepository;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.nio.charset.StandardCharsets;
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
  final Heladera heladeraMock = mock(Heladera.class);
  final Colaborador colaboradorMock = mock(Colaborador.class);
  final SuscripcionRepository repositorio = SuscripcionRepository.getInstancia();

  @BeforeEach
  void setUp() {
    when(heladeraMock.getNombre()).thenReturn("Heladera de testeo");
    when(heladeraMock.getUbicacion()).thenReturn(obelisco);
    when(heladeraMock.getCapacidadEnViandas()).thenReturn(5);
  }

  @AfterEach
  void tearDown() {
    repositorio.deleteTodas();
    SolicitudAperturaPorContribucionRepository.getInstancia().deleteTodas();
    HeladerasRepository.getInstancia().deleteTodas();
    ViandasRepository.getInstancia().deleteTodas();
  }

  @Test
  void testCrearSuscripcion() throws RepositoryException {
    final Ubicacion bibliotecaNacional = new Ubicacion(-34.5844291, -58.4164616);
    when(colaboradorMock.getUbicacion()).thenReturn(bibliotecaNacional);

    SuscripcionController
        .suscribirAHeladera(heladeraMock, MotivoDeDistribucion.FALLA_HELADERA, null, colaboradorMock);

    Optional<Suscripcion> suscripcionOpcional =
        repositorio.get(heladeraMock, MotivoDeDistribucion.FALLA_HELADERA, colaboradorMock);

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
    final Ubicacion centroCivicoBariloche = new Ubicacion(-41.133496, -71.3127926);
    when(colaboradorMock.getUbicacion()).thenReturn(centroCivicoBariloche);

    assertThrows(RuntimeException.class,
        () -> SuscripcionController.suscribirAHeladera(heladeraMock,
            MotivoDeDistribucion.FALLA_HELADERA,
            null,
            colaboradorMock));
  }

  @Test
  void testColaboradoresSonNotificadosDeIncidentes() throws RepositoryException {
    Colaborador colaboradorMock = mock(Colaborador.class);

    final List<Heladera> heladeras = new ArrayList<>(3);
    final HeladerasRepository heladerasRepository = HeladerasRepository.getInstancia();

    for (int i = 0; i < 3; i++) {
      final Heladera heladeraNueva = new Heladera("Heladera " + (i + 1),
          new Ubicacion(-34, -58 - i),
          colaboradorMock,
          10,
          ZonedDateTime.now());

      heladeras.add(heladeraNueva);
      heladerasRepository.insert(heladeraNueva);
    }

    // La ubicación mockeada ayuda a pasar el checkeo de distancia
    when(colaboradorMock.getUbicacion()).thenReturn(new Ubicacion(-34, -58));
    SuscripcionController.suscribirAHeladera(heladeras.get(0),
        MotivoDeDistribucion.FALLA_HELADERA,
        null,
        colaboradorMock);

    ArgumentCaptor<String> capturador = ArgumentCaptor.forClass(String.class);

    IncidenteController.getInstancia().crearAlerta(heladeras.get(0), TipoIncidente.FALLA_CONEXION, ZonedDateTime.now());

    verify(colaboradorMock).enviarMensaje(capturador.capture());

    final String mensajeGenerado = capturador.getValue();

    assertTrue(mensajeGenerado.contains("Se detectó una falla en la heladera Heladera 1"));
    assertTrue(mensajeGenerado.contains("* Heladera 3"));
    assertTrue(mensajeGenerado.contains("* Heladera 2"));
    assertFalse(mensajeGenerado.contains("* Heladera 1"));
  }

  @Test
  void testEnviaNotificacionesAInteresadosEnHeladera() throws SolicitudInvalidaException, RepositoryException {
    Colaborador colaboradorInteresadoPocasViandasMock = mock(Colaborador.class);
    Colaborador colaboradorInteresadoPocoEspacioMock = mock(Colaborador.class);

    SolicitudAperturaPorContribucion solicitudMock = mock(SolicitudAperturaPorContribucion.class);
    when(solicitudMock.getId()).thenReturn(42);
    when(solicitudMock.isVigenteAlMomento(ZonedDateTime.parse("1970-01-01T00:00:00.001Z"), false))
        .thenReturn(true);
    when(solicitudMock.getHeladeraDestino()).thenReturn(heladeraMock);

    SolicitudAperturaPorContribucionRepository.getInstancia().insert(solicitudMock);

    Vianda viandaMock = mock(Vianda.class);
    when(viandaMock.getHeladera()).thenReturn(heladeraMock);

    repositorio.insert(new Suscripcion(heladeraMock,
        MotivoDeDistribucion.FALTAN_VIANDAS,
        4,
        colaboradorInteresadoPocasViandasMock));
    repositorio.insert(new Suscripcion(heladeraMock,
        MotivoDeDistribucion.FALTA_ESPACIO,
        3,
        colaboradorInteresadoPocoEspacioMock));

    ViandasRepository.getInstancia().insert(List.of(viandaMock, viandaMock, viandaMock));

    String jsonAperturaConfirmada = "{\"id\": 42, " +
        "\"esExtraccion\": false, " +
        "\"fechaRealizadaSerializadaIso8601\": \"1970-01-01T00:00:00.001Z\"}";

    SuscripcionController.getInstancia().messageArrived(
        "heladeras/42/solicitudes/confirmadas",
        new MqttMessage(jsonAperturaConfirmada.getBytes(StandardCharsets.UTF_8)));

    String preludioEsperado = "Usted está siendo notificad@ porque está suscrit@ a la heladera " +
        "\"Heladera de testeo\". Se desea informarle que actualmente quedan ";
    String notificacionPocasViandasEsperada = preludioEsperado + "3 viandas para que la heladera quede vacía.";
    String notificacionPocoEspacioEsperada = preludioEsperado + "2 espacios para que la heladera se llene.";

    verify(colaboradorInteresadoPocasViandasMock).enviarMensaje(notificacionPocasViandasEsperada);
    verify(colaboradorInteresadoPocoEspacioMock).enviarMensaje(notificacionPocoEspacioEsperada);
  }
}