package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.controllers.heladera.incidente.IncidenteController;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Email;
import ar.edu.utn.frba.dds.models.entities.contacto.Suscripcion;
import ar.edu.utn.frba.dds.models.entities.contribucion.MotivoDeDistribucion;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.TipoIncidente;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
import ar.edu.utn.frba.dds.models.repositories.ViandasRepository;
import ar.edu.utn.frba.dds.models.repositories.contacto.ContactosRepository;
import ar.edu.utn.frba.dds.models.repositories.contacto.SuscripcionRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.SolicitudAperturaPorContribucionRepository;
import ar.edu.utn.frba.dds.models.repositories.users.UsuariosRepository;
import ar.edu.utn.frba.dds.services.mensajeria.mail.EnviadorMail;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SuscripcionControllerTest {
  final CoordenadasGeograficas obelisco = new CoordenadasGeograficas(-34.5609872, -58.501046);
  final Heladera heladeraMock = mock(Heladera.class);
  final Colaborador colaboradorMock = mock(Colaborador.class);
  final SuscripcionRepository repositorio = SuscripcionRepository.getInstancia();
  final ContactosRepository repositorioContactos = new ContactosRepository();
  final UsuariosRepository repositorioUsuarios = new UsuariosRepository();
  final ViandasRepository viandasRepository = new ViandasRepository();

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
    viandasRepository.deleteTodas();
    repositorioContactos.deleteAll();
    repositorioUsuarios.deleteAll();
  }

  @Test
  void testCrearSuscripcion() throws RepositoryException {
    final CoordenadasGeograficas bibliotecaNacional =
        new CoordenadasGeograficas(-34.5844291, -58.4164616);
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
    final CoordenadasGeograficas centroCivicoBariloche =
        new CoordenadasGeograficas(-41.133496, -71.3127926);
    when(colaboradorMock.getUbicacion()).thenReturn(centroCivicoBariloche);

    assertThrows(RuntimeException.class,
        () -> SuscripcionController.suscribirAHeladera(heladeraMock,
            MotivoDeDistribucion.FALLA_HELADERA,
            null,
            colaboradorMock));
  }

  @Test
  void testColaboradoresSonNotificadosDeIncidentes() throws RepositoryException {
    final List<Heladera> heladeras = new ArrayList<>(3);
    final HeladerasRepository heladerasRepository = HeladerasRepository.getInstancia();

    for (int i = 0; i < 3; i++) {
      final Heladera heladeraNueva = new Heladera("Heladera " + (i + 1),
          new CoordenadasGeograficas(-34d, -58d - i),
          colaboradorMock,
          10,
          ZonedDateTime.now());

      heladeras.add(heladeraNueva);
      heladerasRepository.insert(heladeraNueva);
    }

    // La ubicación mockeada ayuda a pasar el checkeo de distancia
    when(colaboradorMock.getUbicacion()).thenReturn(new CoordenadasGeograficas(-34d, -58d));
    SuscripcionController.suscribirAHeladera(heladeras.get(0),
        MotivoDeDistribucion.FALLA_HELADERA,
        null,
        colaboradorMock);

    Usuario usuario = new Usuario(
        new Documento(TipoDocumento.DNI, 1),
        "",
        "",
        LocalDate.now(),
        new HashSet<>());
    repositorioUsuarios.insert(usuario);

    when(colaboradorMock.getUsuario()).thenReturn(usuario);
    Email email = new Email(usuario, "colaboradormock@example.com");
    Email emailMock = spy(email);
    repositorioContactos.insert(emailMock);

    EnviadorMail emailServiceMock = mock(EnviadorMail.class);

    try (MockedStatic<EnviadorMail> emailService = mockStatic(EnviadorMail.class)) {
      emailService.when(EnviadorMail::getInstancia).thenReturn(emailServiceMock);

      IncidenteController
          .getInstancia()
          .crearAlerta(heladeras.get(0), TipoIncidente.FALLA_CONEXION, ZonedDateTime.now());
    }

    verify(emailServiceMock).enviarMail(
        argThat(destinatario -> Objects.equals(destinatario, "colaboradormock@example.com")),
        argThat(mensaje -> mensaje.contains("Se detectó una falla en la heladera Heladera 1") &&
            mensaje.contains("* Heladera 2") &&
            mensaje.contains("* Heladera 3")));
  }

  /* https://github.com/dds-utn/2024-tpa-ma-ma-grupo-06/issues/217
  @Test
  void testEnviaNotificacionesAInteresadosEnHeladera() throws SolicitudInvalidaException, RepositoryException {
    Colaborador colaboradorInteresadoPocasViandas = new Colaborador(new Documento(TipoDocumento.DNI, 1),
        "",
        "",
        LocalDate.now(),
        new CoordenadasGeograficas(-34.0, -58.0));
    Colaborador colaboradorInteresadoPocoEspacio = new Colaborador(new Documento(TipoDocumento.DNI, 2),
        "",
        "",
        LocalDate.now(),
        new CoordenadasGeograficas(-34.0, -58.0));
    repositorioUsuarios.insert(colaboradorInteresadoPocasViandas.getUsuario());
    repositorioUsuarios.insert(colaboradorInteresadoPocoEspacio.getUsuario());

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
        colaboradorInteresadoPocasViandas));
    repositorio.insert(new Suscripcion(heladeraMock,
        MotivoDeDistribucion.FALTA_ESPACIO,
        3,
        colaboradorInteresadoPocoEspacio));

    ViandasRepository.getInstancia().insert(List.of(viandaMock, viandaMock, viandaMock));

//    Email emailColaboradorInteresadoPocasViandasMock = mock(Email.class);
//    Email emailColaboradorInteresadoPocoEspacioMock = mock(Email.class);
//    ContactosRepository.getInstancia().insert(emailColaboradorInteresadoPocasViandasMock);
//    ContactosRepository.getInstancia().insert(emailColaboradorInteresadoPocoEspacioMock);

    Email emailColaboradorInteresadoPocasViandas =
        new Email(colaboradorInteresadoPocasViandas.getUsuario(), "pocasviandas@example.com");
    Email emailColaboradorInteresadoPocoEspacio =
        new Email(colaboradorInteresadoPocoEspacio.getUsuario(), "pocoespacio@example.com");
    Email emailColaboradorInteresadoPocasViandasMock = spy(emailColaboradorInteresadoPocasViandas);
    Email emailColaboradorInteresadoPocoEspacioMock = spy(emailColaboradorInteresadoPocoEspacio);

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

    verify(emailColaboradorInteresadoPocasViandasMock).enviarMensaje(notificacionPocasViandasEsperada);
    verify(emailColaboradorInteresadoPocoEspacioMock).enviarMensaje(notificacionPocoEspacioEsperada);
  }
  */
}