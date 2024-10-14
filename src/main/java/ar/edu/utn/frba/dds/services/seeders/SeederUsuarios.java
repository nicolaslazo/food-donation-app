package ar.edu.utn.frba.dds.services.seeders;

import ar.edu.utn.frba.dds.models.entities.Tecnico;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Email;
import ar.edu.utn.frba.dds.models.entities.contribucion.Dinero;
import ar.edu.utn.frba.dds.models.entities.contribucion.RubroRecompensa;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.recompensas.Canjeo;
import ar.edu.utn.frba.dds.models.entities.recompensas.Recompensa;
import ar.edu.utn.frba.dds.models.entities.ubicacion.AreaGeografica;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.entities.users.TipoPersonaJuridica;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.TecnicoRepository;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.contacto.ContactosRepository;
import ar.edu.utn.frba.dds.models.repositories.contribucion.DineroRepository;
import ar.edu.utn.frba.dds.models.repositories.recompensas.CanjeosRepository;
import ar.edu.utn.frba.dds.models.repositories.recompensas.RecompensasRepository;
import ar.edu.utn.frba.dds.models.repositories.users.RolesRepository;
import org.apache.commons.codec.digest.DigestUtils;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

public class SeederUsuarios {

  @PostConstruct
  public void seederUsuarios() {

    // ------ ADMINISTRADORES ------
    Usuario usuarioAdmin = new Usuario(
        new Documento(TipoDocumento.DNI, 45111000),
        "Pepe",
        "Pepon",
        LocalDate.now().minusYears(20),
        DigestUtils.sha256Hex("pepe123"),
        new HashSet<>(Set.of(new RolesRepository().findByName("ADMINISTRADOR").get()))
    );
    Email emailUsuario = new Email(usuarioAdmin, "admin@gmail.com");
    new ColaboradorRepository().insert(new Colaborador(usuarioAdmin));
    new ContactosRepository().insert(emailUsuario);

    // ------ COLABORADORES ------
    Colaborador colaboradorFisico = new Colaborador(
        new Documento(TipoDocumento.DNI, 45222000),
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
        new Documento(TipoDocumento.CUIT, 2045111222),
        TipoPersonaJuridica.EMPRESA,
        "Central Nuclear",
        LocalDate.now().minusYears(50),
        "burns123"
    );
    Email emailColaboradorJuridico = new Email(colaboradorJuridico.getUsuario(), "centralNuclear@gmail.com");
    new ColaboradorRepository().insert(colaboradorJuridico);
    new ContactosRepository().insert(emailColaboradorJuridico);

    // ------ COLABORADORES & ADMINISTRADORES ------
    Colaborador colaboradorFisicoAdministrador = new Colaborador(
        new Documento(TipoDocumento.DNI, 45250000),
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
    Email emialSmithers = new Email(colaboradorFisicoAdministrador.getUsuario(), "smithers@gmail.com");
    new ColaboradorRepository().insert(colaboradorFisicoAdministrador);
    new ContactosRepository().insert(emialSmithers);

    Colaborador colaboradorJuridicoAdministrador = new Colaborador(
        new Documento(TipoDocumento.CUIT, 2045250000),
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
        new Documento(TipoDocumento.DNI, 45000111),
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
        new Documento(TipoDocumento.DNI, 45000222),
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

    // ------ AÑADO RECOMPENSAS -------
    Recompensa recompensa1 = new Recompensa("Auriculares", colaboradorJuridico, 100D, 10, RubroRecompensa.ELECTRONICA, null);
    Recompensa recompensa2 = new Recompensa("PC GAMER", colaboradorJuridico, 300D, 3, RubroRecompensa.ELECTRONICA, null);
    Recompensa recompensa3 = new Recompensa("TV", colaboradorJuridicoAdministrador, 400D, 10, RubroRecompensa.ENTRETENIMIENTO, null);
    Canjeo canje1 = new Canjeo(colaboradorJuridico, recompensa1, ZonedDateTime.now());
    new RecompensasRepository().insert(recompensa1);
    new RecompensasRepository().insert(recompensa2);
    new RecompensasRepository().insert(recompensa3);
    new DineroRepository().insert(new Dinero(colaboradorJuridico, 1000, 1));
    new DineroRepository().insert(new Dinero(colaboradorJuridico, 600, 10));
    new CanjeosRepository().insert(canje1);

    // ------ PERSONAS VULNERABLES ------
    //TODO: No tenemos repositorios de personas vulnerables
    //    Usuario usuarioPersonaVulnerable = new Usuario(
    //            new Documento(TipoDocumento.DNI, 45999000),
    //            "Eleanor", //La Loca de los Gatos
    //            "Abernathy",
    //            LocalDate.now().minusYears(60),
    //            DigestUtils.sha256Hex("gatos123"),
    //            new HashSet<>(Set.of(new RolesRepository().findByName("PERSONAVULNERABLE").get())));
    //    DireccionResidencia direccionResidencia = new DireccionResidencia(
    //            usuarioPersonaVulnerable,
    //            "1",
    //            "1",
    //            "1",
    //            "Medrano",
    //            "0000",
    //            "CABA",
    //            "Buenos Aires",
    //            "Argentina"
    //    );
    //    PersonaVulnerable personaVulnerableUno = new PersonaVulnerable(
    //            usuarioPersonaVulnerable,
    //            colaboradorFisico,
    //            ZonedDateTime.now().minusWeeks(1),
    //            direccionResidencia,
    //            5
    //    );
  }
}
