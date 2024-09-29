package ar.edu.utn.frba.dds.controllers.heladera;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.DonacionViandas;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudAperturaPorContribucion;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.entities.users.PermisoDenegadoException;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.HibernatePersistenceReset;
import ar.edu.utn.frba.dds.models.repositories.heladera.SolicitudAperturaPorContribucionRepository;
import ar.edu.utn.frba.dds.models.repositories.users.RolesRepository;
import ar.edu.utn.frba.dds.services.MqttBrokerService;
import ar.edu.utn.frba.dds.services.seeders.SeederRoles;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class SolicitudAperturaPorContribucionControllerTest {
  final MqttBrokerService brokerServiceMock = mock(MqttBrokerService.class);
  Colaborador colaborador;

  Tarjeta tarjeta;
  SolicitudAperturaPorContribucionRepository repositorio;
  DonacionViandas contribucion;

  @BeforeEach
  void setUp() throws PermisoDenegadoException {
    new SeederRoles().seedRoles();

    colaborador = new Colaborador(
            new Documento(TipoDocumento.DNI, 1),
            "",
            "",
            LocalDate.now(),
            new CoordenadasGeograficas(-34d, -58d),
            new RolesRepository().findByName("COLABORADOR").get()
    );

    tarjeta = new Tarjeta(randomUUID());
    repositorio = new SolicitudAperturaPorContribucionRepository();
    contribucion = new DonacionViandas(colaborador,
            Collections.singletonList(new Vianda("",ZonedDateTime.now(),ZonedDateTime.now(), colaborador, 0.0, 22)),
            new Heladera(
                    "",
                    new CoordenadasGeograficas(54.3, 54.0),
                    colaborador,
                    11,
                    ZonedDateTime.now(),
                    ""
            )
    );

    tarjeta.setEnAlta(colaborador.getUsuario(), colaborador, ZonedDateTime.now());
  }

  @AfterEach
  void tearDown() {
    new HibernatePersistenceReset().execute();
  }

  @Test
  void testCreacionFallaSiTarjetaNoTienePermiso() {
    final Tarjeta tarjetaInutil = new Tarjeta(randomUUID());

    assertThrows(PermisoDenegadoException.class,
            () -> new SolicitudAperturaPorContribucionController().crear(tarjetaInutil, mock(DonacionViandas.class)));
  }

  @Test
  void testCreacionFallaSiColaboradorSinDomicilio() {
    colaborador.setUbicacion(null);


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

    assertEquals(1, repositorio.findAll().count());
  }
  /*
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
              Collections.singletonList(new Vianda("",ZonedDateTime.now(),ZonedDateTime.now(), colaboradorMock, 4.0, 22)),
              new Heladera("", new CoordenadasGeograficas(51.3, 52.0), colaboradorMock, 0, ZonedDateTime.now()),
              new Heladera("", new CoordenadasGeograficas(52.3, 51.0), colaboradorMock, 0, ZonedDateTime.now()),
              MotivoDeDistribucion.FALLA_HELADERA);

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
  */
  @Test
  void testSolicitudGuardaSuFechaDeUso() throws Exception {
    SolicitudAperturaPorContribucionController controlador = new SolicitudAperturaPorContribucionController();

    DonacionViandas donacionMock = mock(DonacionViandas.class);
    Heladera heladeraMock = mock(Heladera.class);
    when(heladeraMock.getCapacidadEnViandas()).thenReturn(100);
    when(donacionMock.getDestino()).thenReturn(heladeraMock);

    ZonedDateTime epoch = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC);
    ZonedDateTime unSegundoDespuesDeEpoch = epoch.plusSeconds(1);
    SolicitudAperturaPorContribucion solicitud = new SolicitudAperturaPorContribucion(new Tarjeta(randomUUID()), donacionMock, epoch);

    controlador.repositorio.insert(solicitud);

    controlador.messageArrived(
            "heladeras/1/solicitudes/confirmadas",
            new MqttMessage("{\"id\":%d,\"esExtraccion\":false,\"fechaRealizadaSerializadaIso8601\":\"%s\"}"
                    .formatted(solicitud.getId(),unSegundoDespuesDeEpoch.toString())
                    .getBytes()));

    ZonedDateTime fechaUsada =
            repositorio.findById(solicitud.getId()).get().getFechaAperturaEnDestino();

    assertEquals(unSegundoDespuesDeEpoch, fechaUsada);
  }

}