package ar.edu.utn.frba.dds.controllers.heladera;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Email;
import ar.edu.utn.frba.dds.models.entities.contribucion.DonacionViandas;
import ar.edu.utn.frba.dds.models.entities.contribucion.MovimientoViandas;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.users.PermisoDenegadoException;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.heladera.SolicitudAperturaPorContribucionRepository;
import ar.edu.utn.frba.dds.services.MqttBrokerService;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

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
  final Usuario usuarioMock = mock(Usuario.class);
  final Colaborador colaboradorMock = mock(Colaborador.class);
  final Tarjeta tarjeta = new Tarjeta(randomUUID(), mock(Colaborador.class), usuarioMock);
  final DonacionViandas contribucion = new DonacionViandas(colaboradorMock,
      Collections.singletonList(mock(Vianda.class)),
      mock(Heladera.class));

  @BeforeEach
  void setUp() {
    SolicitudAperturaPorContribucionRepository.getInstancia().deleteTodas();

    when(colaboradorMock.getUsuario()).thenReturn(usuarioMock);
  }

  @Test
  void testCreacionFallaSiTarjetaNoTienePermiso() {
    Usuario duenoInutil = new Usuario(new Email(""), new HashSet<>());
    final Tarjeta tarjetaInutil = new Tarjeta(randomUUID(), mock(Colaborador.class), duenoInutil);

    assertThrows(PermisoDenegadoException.class,
        () -> new SolicitudAperturaPorContribucionController().crear(tarjetaInutil, mock(MovimientoViandas.class)));
  }

  @Test
  void testNoSePuedeCrearConTarjetaDeOtro() {
    final MovimientoViandas contribucionMock = mock(MovimientoViandas.class);
    final Tarjeta tarjetaMock = mock(Tarjeta.class);
    when(contribucionMock.getColaborador()).thenReturn(mock(Colaborador.class));
    when(tarjetaMock.getRecipiente()).thenReturn(mock(Usuario.class));

    assertThrows(PermisoDenegadoException.class,
        () -> new SolicitudAperturaPorContribucionController().crear(tarjetaMock, contribucionMock));
  }

  @Test
  void testCreacionExitosaSeGuardaEnRepositorio() throws MqttException {
    try (MockedStatic<MqttBrokerService> brokerService = mockStatic(MqttBrokerService.class)) {
      brokerService.when(MqttBrokerService::getInstancia).thenReturn(brokerServiceMock);

      new SolicitudAperturaPorContribucionController().crear(tarjeta, contribucion);
    }

    assertEquals(1, SolicitudAperturaPorContribucionRepository.getInstancia().getTodas().size());
  }

  @Test
  void testPublicaCreacionPorMqtt() throws MqttException {
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
}