package ar.edu.utn.frba.dds.services.seeders;

import ar.edu.utn.frba.dds.models.entities.PersonaVulnerable;
import ar.edu.utn.frba.dds.models.entities.Tecnico;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Email;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.ubicacion.AreaGeografica;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.entities.ubicacion.DireccionResidencia;
import ar.edu.utn.frba.dds.models.entities.users.PermisoDenegadoException;
import ar.edu.utn.frba.dds.models.entities.users.TipoPersonaJuridica;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.PersonaVulnerableRepository;
import ar.edu.utn.frba.dds.models.repositories.TecnicoRepository;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.contacto.ContactosRepository;
import ar.edu.utn.frba.dds.models.repositories.documentacion.TarjetasRepository;
import ar.edu.utn.frba.dds.models.repositories.users.RolesRepository;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SeederUsuarios {
  public static void execute() throws PermisoDenegadoException {
    // ------ ADMINISTRADORES ------
    Usuario usuarioAdmin = new Usuario(
        new Documento(TipoDocumento.DNI, 1),
        "Pepe",
        "Pepon",
        LocalDate.now().minusYears(20),
        DigestUtils.sha256Hex("pepe123"),
        new HashSet<>(Set.of(new RolesRepository().findByName("ADMINISTRADOR").get()))
    );
    Email emailUsuario = new Email(usuarioAdmin, "admin@gmail.com");
    Colaborador colaboradorAdmin = new Colaborador(usuarioAdmin);
    colaboradorAdmin.setUbicacion(new CoordenadasGeograficas(-34d, -58d));

    new ColaboradorRepository().insert(colaboradorAdmin);
    new ContactosRepository().insert(emailUsuario);

    // ------ COLABORADORES ------
    Colaborador colaboradorFisico = new Colaborador(
        new Documento(TipoDocumento.DNI, 2),
        "Osvaldo",
        "Benitez",
        LocalDate.now().minusYears(22),
        new CoordenadasGeograficas(54D, 54D),
        "osvaldo123",
        new RolesRepository().findByName("COLABORADORFISICO").get()
    );
    Email emailColaboradorFisico = new Email(colaboradorFisico.getUsuario(), "colaboradorFisico@gmail.com");
    new ColaboradorRepository().insert(colaboradorFisico);
    new ContactosRepository().insert(emailColaboradorFisico);

    Colaborador colaboradorJuridico = new Colaborador(
        new Documento(TipoDocumento.CUIT, 1),
        TipoPersonaJuridica.EMPRESA,
        "Central Nuclear",
        LocalDate.now().minusYears(50),
        "burns123"
    );
    Email emailColaboradorJuridico = new Email(colaboradorJuridico.getUsuario(), "centralNuclear@gmail.com");
    new ColaboradorRepository().insert(colaboradorJuridico);
    new ContactosRepository().insert(emailColaboradorJuridico);

    Colaborador colaboradorFraudulento = new Colaborador(
        new Documento(TipoDocumento.DNI, 3),
        "Colaborador",
        "Fraudulento",
        LocalDate.now().minusYears(22),
        new CoordenadasGeograficas(-34D, -58D),
        "fraude",
        new RolesRepository().findByName("COLABORADORFISICO").get()
    );
    Email emailColaboradorFraudulento =
        new Email(colaboradorFraudulento.getUsuario(), "colaboradorFraudulento@mail.com");
    new ColaboradorRepository().insert(colaboradorFraudulento);
    new ContactosRepository().insert(emailColaboradorFraudulento);

    // ------ COLABORADORES & ADMINISTRADORES ------
    Colaborador colaboradorFisicoAdministrador = new Colaborador(
        new Documento(TipoDocumento.DNI, 4),
        "Waylon",
        "Smithers",
        LocalDate.now().minusYears(40),
        new CoordenadasGeograficas(54D, 54D),
        "smithers",
        new RolesRepository().findByName("COLABORADORFISICO").get()
    );
    colaboradorFisicoAdministrador.getUsuario().agregarRol(
        new RolesRepository().findByName("ADMINISTRADOR").get()
    );
    Email emailSmithers = new Email(colaboradorFisicoAdministrador.getUsuario(), "smithers@gmail.com");
    new ColaboradorRepository().insert(colaboradorFisicoAdministrador);
    new ContactosRepository().insert(emailSmithers);

    Colaborador colaboradorJuridicoAdministrador = new Colaborador(
        new Documento(TipoDocumento.CUIT, 2),
        TipoPersonaJuridica.INSTITUCION,
        "Escuela Primaria Springfield",
        LocalDate.now().minusYears(60),
        "skinner"
    );
    colaboradorJuridicoAdministrador.getUsuario().agregarRol(
        new RolesRepository().findByName("ADMINISTRADOR").get()
    );
    Email emailEscuela = new Email(colaboradorJuridicoAdministrador.getUsuario(), "escuela@gmail.com");
    new ColaboradorRepository().insert(colaboradorJuridicoAdministrador);
    new ContactosRepository().insert(emailEscuela);

    // ------ TÉCNICOS ------
    Tecnico tecnicoUno = new Tecnico(
        new Documento(TipoDocumento.DNI, 5),
        "Homero",
        "Simpson",
        LocalDate.now().minusYears(36),
        "20-45000111-9",
        new AreaGeografica(new CoordenadasGeograficas(54D, 54D), 100),
        DigestUtils.sha256Hex("homero123"),
        new RolesRepository().findByName("TECNICO").get()
    );
    Email emailTecnicoUno = new Email(tecnicoUno.getUsuario(), "tecnicoHomero@gmail.com");
    new TecnicoRepository().insert(tecnicoUno);
    new ContactosRepository().insert(emailTecnicoUno);

    Tecnico tecnicoDos = new Tecnico(
        new Documento(TipoDocumento.DNI, 6),
        "Carl",
        "Carlson",
        LocalDate.now().minusYears(34),
        "20-45000222-9",
        new AreaGeografica(new CoordenadasGeograficas(54D, 54D), 100),
        DigestUtils.sha256Hex("carl123"),
        new RolesRepository().findByName("TECNICO").get()
    );
    Email emailTecnicoDos = new Email(tecnicoDos.getUsuario(), "tecnicoCarl@gmail.com");
    new TecnicoRepository().insert(tecnicoDos);
    new ContactosRepository().insert(emailTecnicoDos);

    Usuario usuarioPersonaVulnerable = new Usuario(
        new Documento(TipoDocumento.DNI, 7),
        "Eleanor", //La Loca de los Gatos
        "Abernathy",
        LocalDate.now().minusYears(60),
        DigestUtils.sha256Hex("gatos123"),
        new HashSet<>(Set.of(new RolesRepository().findByName("PERSONAVULNERABLE").get())));
    DireccionResidencia direccionResidencia = new DireccionResidencia(
        usuarioPersonaVulnerable,
        "1",
        "1",
        "1",
        "Medrano",
        "0000",
        "CABA",
        "Buenos Aires",
        "Argentina"
    );
    PersonaVulnerable personaVulnerableUno = new PersonaVulnerable(
        usuarioPersonaVulnerable,
        colaboradorFisico,
        ZonedDateTime.now().minusWeeks(1),
        direccionResidencia,
        5
    );
    Email emailPersonaVulnerableUno = new Email(usuarioPersonaVulnerable, "personaVulnerable@gmail.com");
    new PersonaVulnerableRepository().insert(personaVulnerableUno);
    new ContactosRepository().insert(emailPersonaVulnerableUno);

    Tarjeta tarjeta1 = new Tarjeta(UUID.fromString("12345678-1234-1234-1234-123456789abc"));
    Tarjeta tarjeta2 = new Tarjeta(UUID.fromString("12345678-2345-2345-2345-123456789abc"));
    Tarjeta tarjeta3 = new Tarjeta(UUID.fromString("12345678-3456-3456-3456-123456789abc"));

    tarjeta1.setEnAlta(usuarioAdmin, colaboradorAdmin, ZonedDateTime.now());

    new TarjetasRepository().insertAll(Set.of(tarjeta1, tarjeta2, tarjeta3));
  }
}
