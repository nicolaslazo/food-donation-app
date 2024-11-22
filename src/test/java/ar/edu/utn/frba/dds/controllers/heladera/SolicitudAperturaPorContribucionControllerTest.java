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
import ar.edu.utn.frba.dds.models.repositories.ViandasRepository;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.contribucion.DonacionViandasRepository;
import ar.edu.utn.frba.dds.models.repositories.documentacion.TarjetasRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.SolicitudAperturaPorContribucionRepository;
import ar.edu.utn.frba.dds.models.repositories.users.RolesRepository;
import ar.edu.utn.frba.dds.server.Initializer;
import ar.edu.utn.frba.dds.services.MqttBrokerService;
import ar.edu.utn.frba.dds.services.seeders.SeederRoles;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SolicitudAperturaPorContribucionControllerTest {
  final MqttBrokerService brokerServiceMock = mock(MqttBrokerService.class);
  Colaborador colaborador;

  Tarjeta tarjeta;
  SolicitudAperturaPorContribucionRepository repositorio;
  Vianda vianda;
  DonacionViandas contribucion;
  Heladera heladera;

  public static void main(String[] args) throws MqttException, PermisoDenegadoException, IOException {
    // Para testear la integración con HiveMQ. El servidor tiene que estar corriendo
    Initializer.init();

    Tarjeta tarjeta = new TarjetasRepository().findAll().findFirst().get();
    Colaborador colaborador = new ColaboradorRepository().findAll().findFirst().get();
    Vianda vianda = new ViandasRepository().findAll().findFirst().get();
    Heladera heladera = new HeladerasRepository().findAll().findFirst().get();
    DonacionViandas donacion = new DonacionViandas(colaborador, Set.of(vianda), heladera);

    System.out.printf("El topic es heladeras/%d/solicitudes. Apretá enter%n", heladera.getId());
    System.in.read();

    SolicitudAperturaPorContribucion solicitud = new SolicitudAperturaPorContribucionController().crear(tarjeta, donacion);

    System.out.println("Id de solicitud es " + solicitud.getId());
  }

  @BeforeEach
  void setUp() throws PermisoDenegadoException {
    new SeederRoles().seedRoles();

    colaborador = new Colaborador(
        new Documento(TipoDocumento.DNI, 1),
        "",
        "",
        LocalDate.now(),
        new CoordenadasGeograficas(-34d, -58d),
        null,
        new RolesRepository().findByName("COLABORADORFISICO").get()
    );
    new ColaboradorRepository().insert(colaborador);

    tarjeta = new Tarjeta(randomUUID());
    repositorio = new SolicitudAperturaPorContribucionRepository();
    vianda = new Vianda("",
        ZonedDateTime.now(),
        ZonedDateTime.now(),
        colaborador,
        0.0,
        22);
    new ViandasRepository().insert(vianda);

    heladera = new Heladera(
        "",
        new CoordenadasGeograficas(54.3, 54.0),
        colaborador,
        11,
        ZonedDateTime.now(),
        ""
    );
    new HeladerasRepository().insert(heladera);

    contribucion = new DonacionViandas(colaborador,
        Collections.singletonList(vianda),
        heladera
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
    new ColaboradorRepository().insert(colaborador);
    new HeladerasRepository().insert(heladera);
    new ViandasRepository().insert(vianda);
    new DonacionViandasRepository().insert(contribucion);
    new TarjetasRepository().insert(tarjeta);
    try (MockedStatic<MqttBrokerService> brokerService = mockStatic(MqttBrokerService.class)) {
      brokerService.when(MqttBrokerService::getInstancia).thenReturn(brokerServiceMock);

      new SolicitudAperturaPorContribucionController().crear(tarjeta, contribucion);
    }

    assertEquals(1, repositorio.findAll().count());
  }

  @Test
  void testPublicaCreacionPorMqttParaDonacion() throws MqttException, PermisoDenegadoException {
    try (MockedStatic<MqttBrokerService> brokerService = mockStatic(MqttBrokerService.class)) {
      brokerService.when(MqttBrokerService::getInstancia).thenReturn(brokerServiceMock);

      new SolicitudAperturaPorContribucionController().crear(tarjeta, contribucion);
    }

    verify(brokerServiceMock)
        .publicar(argThat(topic -> Objects.equals(topic, "heladera/%d/solicitud/contribucion".formatted(heladera.getId()))),
            argThat(payload -> payload.startsWith("{") &&
                payload.endsWith("}") &&
                payload.contains("identificadorTarjeta") &&
                payload.contains("idSolicitud") &&
                payload.contains("fechaVencimientoSerializadaIso8601") &&
                payload.contains("pesosViandasEnGramos")));
  }

  /*
  @Test
  void testSolicitudDeAperturaParaRedistribucionMandaDosMensajes() throws MqttException, PermisoDenegadoException {
    Heladera destino = new Heladera("",
        new CoordenadasGeograficas(51.3, 52.0),
        colaborador,
        0,
        ZonedDateTime.now(),
        "Barrio");
    Heladera origen = new Heladera("",
        new CoordenadasGeograficas(52.3, 51.0),
        colaborador,
        0,
        ZonedDateTime.now(),
        "Barrio");
    new HeladerasRepository().insertAll(List.of(origen, destino));

    Vianda vianda = new Vianda("",
        ZonedDateTime.now(),
        ZonedDateTime.now(),
        colaborador,
        4.0,
        22);
    new ViandasRepository().insert(vianda);

    RedistribucionViandas redistribucion = new RedistribucionViandas(colaborador,
        List.of(vianda),
        destino,
        origen,
        MotivoDeDistribucion.FALLA_HELADERA);

    try (MockedStatic<MqttBrokerService> brokerService = mockStatic(MqttBrokerService.class)) {
      brokerService.when(MqttBrokerService::getInstancia).thenReturn(brokerServiceMock);

      assertInstanceOf(MqttBrokerService.class, MqttBrokerService.getInstancia());

      new SolicitudAperturaPorContribucionController().crear(tarjeta, redistribucion);
    }

    verify(brokerServiceMock, times(2))
        .publicar(argThat(topic -> Objects.equals(topic, "heladera/%d/solicitud/contribucion/confirmada".formatted(destino.getId()))),
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
        "heladera/%d/solicitud/contribucion/confirmada".formatted(heladera.getId()),
        new MqttMessage("{\"id\":%d,\"esExtraccion\":false,\"fechaRealizadaSerializadaIso8601\":\"%s\"}"
            .formatted(solicitud.getId(), unSegundoDespuesDeEpoch.toString())
            .getBytes()));

    ZonedDateTime fechaUsada =
        repositorio.findById(solicitud.getId()).get().getFechaAperturaEnDestino();

    assertEquals(unSegundoDespuesDeEpoch, fechaUsada);
  }
}