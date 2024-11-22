package ar.edu.utn.frba.dds.controllers.heladera;

import ar.edu.utn.frba.dds.models.entities.PersonaVulnerable;
import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudAperturaPorConsumicion;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.entities.users.PermisoDenegadoException;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.HibernatePersistenceReset;
import ar.edu.utn.frba.dds.models.repositories.PersonaVulnerableRepository;
import ar.edu.utn.frba.dds.models.repositories.ViandasRepository;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.documentacion.TarjetasRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.SolicitudAperturaPorConsumicionRepository;
import ar.edu.utn.frba.dds.models.repositories.users.RolesRepository;
import ar.edu.utn.frba.dds.models.repositories.users.UsuariosRepository;
import ar.edu.utn.frba.dds.services.MqttBrokerService;
import ar.edu.utn.frba.dds.services.seeders.SeederRoles;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SolicitudAperturaPorConsumicionControllerTest {
  final MqttBrokerService brokerServiceMock = mock(MqttBrokerService.class);
  PersonaVulnerable personaVulnerable;
  Tarjeta tarjeta;
  SolicitudAperturaPorConsumicionRepository repositorio;
  Vianda vianda;
  Heladera heladera;

  @BeforeEach
  void setUp() throws PermisoDenegadoException {
    new SeederRoles().seedRoles();

    CoordenadasGeograficas obelisco = new CoordenadasGeograficas(-34.5611745, -58.4287506);
    Colaborador colaboradorFisico = new Colaborador(
        new Documento(TipoDocumento.DNI, 12345678),
        "Juan",
        "Perez",
        LocalDate.now(),
        obelisco
    );
    new ColaboradorRepository().insert(colaboradorFisico);

    Usuario usuarioPersonaVulnerable = new Usuario(
        new Documento(TipoDocumento.DNI, 87654321),
        "Maria",
        "Lopez",
        LocalDate.now(),
        null,
        new HashSet<>(List.of(new RolesRepository().findByName("PERSONAVULNERABLE").get()))
    );
    new UsuariosRepository().insert(usuarioPersonaVulnerable);

    personaVulnerable = new PersonaVulnerable(
        usuarioPersonaVulnerable,
        colaboradorFisico,
        ZonedDateTime.now().minusWeeks(1),
        null,
        0
    );
    new PersonaVulnerableRepository().insert(personaVulnerable);

    // Crear heladera
    heladera = new Heladera(
        "Heladera Medrano",
        new CoordenadasGeograficas(-34.5984145, -58.4204851),
        colaboradorFisico,
        100,
        ZonedDateTime.now(),
        "Almagro"
    );
    new HeladerasRepository().insert(heladera);

    // Crear tarjeta y vianda
    tarjeta = new Tarjeta(randomUUID());
    repositorio = new SolicitudAperturaPorConsumicionRepository();

    vianda = new Vianda("",
        ZonedDateTime.now(),
        ZonedDateTime.now(),
        colaboradorFisico,
        0.0,
        22);
    vianda.setHeladera(heladera);
    new ViandasRepository().insert(vianda);

    tarjeta.setEnAlta(usuarioPersonaVulnerable, colaboradorFisico, ZonedDateTime.now());
    new TarjetasRepository().insert(tarjeta);
  }

  @AfterEach
  void tearDown() {
    new HibernatePersistenceReset().execute();
  }

  @Test
  void testCreacionFallaSiTarjetaNoTienePermiso() {
    final Tarjeta tarjetaInutil = new Tarjeta(randomUUID());

    assertThrows(PermisoDenegadoException.class,
        () -> new SolicitudAperturaPorConsumicionController().crear(tarjetaInutil, mock(Vianda.class)));
  }

  @Test
  void testCreacionFallaSiSeAlcanzaLimiteViandas() {
    try (MockedConstruction<SolicitudAperturaPorConsumicionRepository> ignored =
             mockConstruction(SolicitudAperturaPorConsumicionRepository.class, (mock, context) ->
                 when(mock.findCantidadUsadasHoy(tarjeta))
                     .thenReturn(personaVulnerable.getCantidadViandasPermitidasPorDia()))) {
      assertThrows(PermisoDenegadoException.class,
          () -> new SolicitudAperturaPorConsumicionController().crear(tarjeta, vianda));
    }
  }

  @Test
  void testPublicaCreacionPorMqtt() throws MqttException, PermisoDenegadoException {
    try (MockedStatic<MqttBrokerService> brokerService = mockStatic(MqttBrokerService.class)) {
      brokerService.when(MqttBrokerService::getInstancia).thenReturn(brokerServiceMock);

      new SolicitudAperturaPorConsumicionController().crear(tarjeta, vianda);
    }

    verify(brokerServiceMock)
        .publicar(
            argThat(topic -> topic.equals("heladera/" + heladera.getId() + "/solicitud/consumicion")),
            argThat(payload -> payload.startsWith("{") &&
                payload.endsWith("}") &&
                payload.contains("identificadorTarjeta") &&
                payload.contains("idSolicitud") &&
                payload.contains("fechaVencimientoSerializadaIso8601"))
        );
  }

  @Test
  void testSolicitudGuardaSuFechaDeUso() throws Exception {
    SolicitudAperturaPorConsumicionController controlador = new SolicitudAperturaPorConsumicionController();

    ZonedDateTime epoch = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC);
    ZonedDateTime unSegundoDespuesDeEpoch = epoch.plusSeconds(1);
    SolicitudAperturaPorConsumicion solicitud = new SolicitudAperturaPorConsumicion(
        tarjeta,
        vianda,
        epoch
    );

    controlador.repositorio.insert(solicitud);

    controlador.messageArrived(
        "heladeras/" + heladera.getId() + "/solicitud/consumicion/confirmada",
        new MqttMessage(String.format(
            "{\"id\":%d,\"fechaRealizadaSerializadaIso8601\":\"%s\"}",
            solicitud.getId(),
            unSegundoDespuesDeEpoch
        ).getBytes())
    );

    ZonedDateTime fechaUsada = repositorio.findById(solicitud.getId()).get().getFechaUsada();
    assertEquals(unSegundoDespuesDeEpoch, fechaUsada);
  }
}
