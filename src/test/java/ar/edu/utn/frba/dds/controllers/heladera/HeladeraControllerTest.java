package ar.edu.utn.frba.dds.controllers.heladera;

import ar.edu.utn.frba.dds.models.entities.Tecnico;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.AreaGeografica;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.entities.users.Rol;
import ar.edu.utn.frba.dds.models.repositories.HibernatePersistenceReset;
import ar.edu.utn.frba.dds.models.repositories.TecnicoRepository;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.users.RolesRepository;
import ar.edu.utn.frba.dds.services.seeders.SeederRoles;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class HeladeraControllerTest {
  Colaborador colaborador = new Colaborador(new Documento(TipoDocumento.DNI, 1),
      "",
      "",
      LocalDate.now(),
      null);

  Heladera heladeraMock = Mockito.mock(Heladera.class);
  CoordenadasGeograficas obelisco =
      new CoordenadasGeograficas(-34.603706013664166, -58.3815728218273);
  CoordenadasGeograficas aCienMetrosDelObelisco =
      new CoordenadasGeograficas(-34.60375463775254, -58.38264297552039);
  Rol rolTecnico;


  @BeforeEach
  void setUp() {
    new ColaboradorRepository().insert(colaborador);

    new SeederRoles().seedRoles();
    rolTecnico = new RolesRepository().findByName("TECNICO").get();

    when(heladeraMock.getUbicacion()).thenReturn(obelisco);
  }

  @AfterEach
  void tearDown() {
    new HibernatePersistenceReset().execute();
  }

  @Test
  void testDevuelveNadaSiNoEncuentraTecnicosProximos() {
    assertTrue(HeladeraController.encontrarTecnicoMasCercano(heladeraMock).isEmpty());
  }

  @Test
  void testDevuelveNadaSiLosTecnicosProximosNoLleganEnRango() {
    new TecnicoRepository().insert(
        new Tecnico(
            new Documento(TipoDocumento.DNI, 1),
            "",
            "",
            LocalDate.now(),
            "123",
            new AreaGeografica(aCienMetrosDelObelisco, 50f),
            "123",
            rolTecnico)
    );
    assertTrue(HeladeraController.encontrarTecnicoMasCercano(heladeraMock).isEmpty());
  }

  @Test
  void testPriorizaTecnicoMasCercanoEnCasoDeVariosDisponibles() {
    final CoordenadasGeograficas aCincuentaMetrosDelObelisco =
        new CoordenadasGeograficas(-34.603725171426916, -58.38211380719743);
    Tecnico tecnicoDeseado = new Tecnico(
        new Documento(TipoDocumento.DNI, 1),
        "",
        "",
        LocalDate.now(),
        "123",
        new AreaGeografica(aCincuentaMetrosDelObelisco, 1000f),
        "123",
        rolTecnico
    );
    new TecnicoRepository().insert(tecnicoDeseado);
    new TecnicoRepository().insert(
        new Tecnico(
            new Documento(TipoDocumento.DNI, 1),
            "",
            "",
            LocalDate.now(),
            "456",
            new AreaGeografica(aCienMetrosDelObelisco, 1000f),
            "123",
            rolTecnico)
    );

    assertEquals(Optional.of(tecnicoDeseado), HeladeraController.encontrarTecnicoMasCercano(heladeraMock));
  }

//   TODO: Arreglar despues, no tengo tiempo para renegar con esto
//   @Test
//   void testNotificaTecnicoMasCercanoDeIncidentes() {
//     CoordenadasGeograficas coordenadas = new CoordenadasGeograficas(-34d, -58d);
//
//     Heladera heladera = new Heladera("Heladera a testear",
//         coordenadas,
//         colaborador,
//         10,
//         ZonedDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC));
//
//     Tecnico tecnico = new Tecnico(
//             new Documento(TipoDocumento.DNI, 123),
//             "",
//             "",
//             LocalDate.now(),
//             "1",
//             new AreaGeografica( obelisco, 100)
//     );
//
//     new HeladerasRepository().insert(heladera);
//     new TecnicoRepository().insert(tecnico);
//
//     Usuario usuario = new Usuario(new Documento(TipoDocumento.DNI, 1),
//         "",
//         "",
//         LocalDate.now(),
//         new HashSet<>());
//
//     new UsuariosRepository().insert(usuario);
//
//     Email email = new Email(usuario, "tecnicomock@example.com");
//
//     new ContactosRepository().insert(email);
//
//     EnviadorMail emailServiceMock = mock(EnviadorMail.class);
//
//     try (MockedStatic<EnviadorMail> emailService = mockStatic(EnviadorMail.class)) {
//       emailService.when(EnviadorMail::getInstancia).thenReturn(emailServiceMock);
//
//       IncidenteController
//           .getInstancia()
//           .crearAlerta(heladera,
//               TipoIncidente.FALLA_CONEXION,
//               ZonedDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC).plusMinutes(5));
//     }
//
//     verify(emailServiceMock)
//         .enviarMail("tecnicomock@example.com",
//             "[ALERTA] la heladera \"Heladera a testear\" tuvo un incidente el 1970-01-01T00:05Z. " +
//                 "Por favor acercarse a la brevedad");
//   }

  @Test
  void testEncuentraHeladerasCercanas() {
    final List<Heladera> heladeras = new ArrayList<>(5);
    for (int i = 0; i < 5; i++) {
      heladeras.add(new Heladera("",
          new CoordenadasGeograficas(-34d, -58d - i),
          colaborador,
          10,
          ZonedDateTime.now(),
          ""));
    }

    final Heladera heladeraTarget = heladeras.get(0);

    Collections.shuffle(heladeras);

    for (Heladera heladera : heladeras) new HeladerasRepository().insert(heladera);

    final List<Heladera> sugerencias = new HeladeraController().encontrarHeladerasCercanas(heladeraTarget);
    final double[] longitudes =
        sugerencias.stream().mapToDouble(sugerencia -> sugerencia.getUbicacion().getLongitud()).toArray();

    assertEquals(-59d, longitudes[0]);
    assertEquals(-60d, longitudes[1]);
    assertEquals(-61d, longitudes[2]);
  }
}