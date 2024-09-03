package ar.edu.utn.frba.dds.controllers.heladera;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.DonacionViandas;
import ar.edu.utn.frba.dds.models.entities.contribucion.MotivoDeDistribucion;
import ar.edu.utn.frba.dds.models.entities.contribucion.RedistribucionViandas;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudAperturaPorContribucion;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.entities.users.PermisoDenegadoException;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.heladera.SolicitudAperturaPorContribucionRepository;
import ar.edu.utn.frba.dds.services.MqttBrokerService;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SolicitudAperturaPorContribucionControllerTest {
  final MqttBrokerService brokerServiceMock = mock(MqttBrokerService.class);
  final Usuario usuarioMock = mock(Usuario.class);
  final Colaborador colaboradorMock = mock(Colaborador.class);
  final Tarjeta tarjeta = new Tarjeta(randomUUID());
  final DonacionViandas contribucion = new DonacionViandas(colaboradorMock,
      Collections.singletonList(mock(Vianda.class)),
      mock(Heladera.class));

  @BeforeEach
  void setUp() throws PermisoDenegadoException {
    tarjeta.setEnAlta(usuarioMock, colaboradorMock, ZonedDateTime.now());

    when(colaboradorMock.getUsuario()).thenReturn(usuarioMock);
    when(colaboradorMock.getUbicacion()).thenReturn(new CoordenadasGeograficas(-34d, -58d));
  }

  @AfterEach
  void tearDown() {
    SolicitudAperturaPorContribucionRepository.getInstancia().deleteTodas();
  }

  @Test
  void testCreacionFallaSiTarjetaNoTienePermiso() {
    final Tarjeta tarjetaInutil = new Tarjeta(randomUUID());

    assertThrows(PermisoDenegadoException.class,
        () -> new SolicitudAperturaPorContribucionController().crear(tarjetaInutil, mock(DonacionViandas.class)));
  }

  @Test
  void testCreacionFallaSiColaboradorSinDomicilio() {
    when(colaboradorMock.getUbicacion()).thenReturn(null);

    assertThrows(PermisoDenegadoException.class,
        () -> new SolicitudAperturaPorContribucionController().crear(tarjeta, contribucion));
  }

  @Test
  void testNoSePuedeCrearConTarjetaDeOtro() {
    final DonacionViandas contribucionMock = mock(DonacionViandas.class);
    final Tarjeta tarjetaMock = mock(Tarjeta.class);
    when(contribucionMock.getColaborador()).thenReturn(mock(Colaborador.class));
    when(tarjetaMock.getRecipiente()).thenReturn(mock(Usuario.class));

    assertThrows(PermisoDenegadoException.class,
        () -> new SolicitudAperturaPorContribucionController().crear(tarjetaMock, contribucionMock));
  }

  @Test
  void testCreacionExitosaSeGuardaEnRepositorio() throws MqttException, PermisoDenegadoException {
    try (MockedStatic<MqttBrokerService> brokerService = mockStatic(MqttBrokerService.class)) {
      brokerService.when(MqttBrokerService::getInstancia).thenReturn(brokerServiceMock);

      new SolicitudAperturaPorContribucionController().crear(tarjeta, contribucion);
    }

    assertEquals(1, SolicitudAperturaPorContribucionRepository.getInstancia().getTodas().count());
  }

  @Test
  void testPublicaCreacionPorMqttParaDonacion() throws MqttException, PermisoDenegadoException {
    try (MockedStatic<MqttBrokerService> brokerService = mockStatic(MqttBrokerService.class)) {
      brokerService.when(MqttBrokerService::getInstancia).thenReturn(brokerServiceMock);

      new SolicitudAperturaPorContribucionController().crear(tarjeta, contribucion);
    }

    verify(brokerServiceMock)
        .publicar(argThat(topic -> Objects.equals(topic, "heladeras/0/solicitudes")),
            argThat(payload -> payload.startsWith("{") &&
                payload.endsWith("}") &&
                payload.contains("identificadorTarjeta") &&
                payload.contains("idSolicitud") &&
                payload.contains("fechaVencimientoSerializadaIso8601") &&
                payload.contains("pesosViandasEnGramos")));
  }

  @Test
  void testSolicitudDeAperturaParaRedistribucionMandaDosMensajes() throws MqttException, PermisoDenegadoException {
    RedistribucionViandas redistribucion = new RedistribucionViandas(colaboradorMock,
        List.of(mock(Vianda.class)),
        mock(Heladera.class),
        mock(Heladera.class),
        mock(MotivoDeDistribucion.class));

    try (MockedStatic<MqttBrokerService> brokerService = mockStatic(MqttBrokerService.class)) {
      brokerService.when(MqttBrokerService::getInstancia).thenReturn(brokerServiceMock);

      assertInstanceOf(MqttBrokerService.class, MqttBrokerService.getInstancia());

      new SolicitudAperturaPorContribucionController().crear(tarjeta, redistribucion);
    }

    verify(brokerServiceMock, times(2))
        .publicar(argThat(topic -> Objects.equals(topic, "heladeras/0/solicitudes")),
            argThat(payload -> payload.startsWith("{") &&
                payload.endsWith("}") &&
                payload.contains("identificadorTarjeta") &&
                payload.contains("idSolicitud") &&
                payload.contains("fechaVencimientoSerializadaIso8601") &&
                payload.contains("pesosViandasEnGramos")));
  }

  @Test
  void testSolicitudGuardaSuFechaDeUso() throws Exception {
    SolicitudAperturaPorContribucionController controlador = new SolicitudAperturaPorContribucionController();

    DonacionViandas donacionMock = mock(DonacionViandas.class);
    Heladera heladeraMock = mock(Heladera.class);
    when(heladeraMock.getCapacidadEnViandas()).thenReturn(100);
    when(donacionMock.getDestino()).thenReturn(heladeraMock);

    ZonedDateTime epoch = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC);
    ZonedDateTime unSegundoDespuesDeEpoch = epoch.plusSeconds(1);
    SolicitudAperturaPorContribucion solicitud = new SolicitudAperturaPorContribucion(mock(Tarjeta.class), donacionMock, epoch);


    controlador.messageArrived(
        "heladeras/1/solicitudes/confirmadas",
        new MqttMessage("{\"id\":1,\"esExtraccion\":false,\"fechaRealizadaSerializadaIso8601\":\"%s\"}"
            .formatted(unSegundoDespuesDeEpoch.toString())
            .getBytes()));

    ZonedDateTime fechaUsada =
        SolicitudAperturaPorContribucionRepository.getInstancia().findById(solicitud.getId()).get().getFechaAperturaEnDestino();

    assertEquals(unSegundoDespuesDeEpoch, fechaUsada);
  }
}