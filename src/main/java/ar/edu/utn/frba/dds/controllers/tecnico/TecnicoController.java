package ar.edu.utn.frba.dds.controllers.tecnico;

import ar.edu.utn.frba.dds.dtos.input.tecnico.TecnicoInputDTO;
import ar.edu.utn.frba.dds.models.entities.Tecnico;
import ar.edu.utn.frba.dds.models.entities.contacto.Contacto;
import ar.edu.utn.frba.dds.models.entities.contacto.Email;
import ar.edu.utn.frba.dds.models.entities.contacto.Telegram;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.ubicacion.AreaGeografica;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.entities.users.PermisoDenegadoException;
import ar.edu.utn.frba.dds.models.entities.users.Rol;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.TecnicoRepository;
import ar.edu.utn.frba.dds.models.repositories.contacto.ContactosRepository;
import ar.edu.utn.frba.dds.models.repositories.users.PermisosRepository;
import ar.edu.utn.frba.dds.models.repositories.users.RolesRepository;
import ar.edu.utn.frba.dds.models.repositories.users.UsuariosRepository;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


public class TecnicoController {
  TecnicoRepository tecnicoRepository = new TecnicoRepository();
  PermisosRepository permisosRepository = new PermisosRepository();
  RolesRepository rolesRepository = new RolesRepository();

  public void crearTecnico(TecnicoInputDTO data, Usuario usuarioCreador) throws PermisoDenegadoException {
    usuarioCreador.assertTienePermiso(
            "Crear-Tecnico",
            "No tienes permisos para crear un nuevo tecnico"
    );

    Rol rolTecnico = rolesRepository.findByName("TECNICO").get();
    Tecnico newTecnico = new Tecnico(
            data.getDocumento(),
            data.getPrimerNombre(),
            data.getApellido(),
            data.getFechaNacimiento(),
            data.getCuil(),
            data.getAreaGeografica(),
            data.getContrasenia(),
            rolTecnico
    );
    tecnicoRepository.insert(newTecnico);
  }

  public void index(Context context) {
    Map<String, Object> model = context.attribute("model");
    context.render("agregartecnico/agregartecnico.hbs", model);
  }

  public void create(Context context) {
    Documento documento =
        new Documento(
            TipoDocumento.fromString(context.formParam("documento")),
            Integer.parseInt(context.formParam("numeroDocumento")));

    Rol rolTecnico = new RolesRepository().findByName("TECNICO")
        .orElseThrow(() -> new IllegalArgumentException("Rol 'TECNICO' no encontrado."));

    Usuario usuario = new Usuario(
        documento,
        context.formParam("nombre"),
        context.formParam("apellido"),
        LocalDate.parse(context.formParam("dob")),
        DigestUtils.sha256Hex(context.formParam("password")),
        new HashSet<>(List.of(rolTecnico))
    );

    new UsuariosRepository().insert(usuario);

    ContactosRepository contactosRepository = new ContactosRepository();
    List<Contacto> contactos = new ArrayList<>();

    String email = context.formParam("email");
    if (email != null && !email.isBlank()) {
      Contacto contactoEmail = new Email(usuario, email);
      contactos.add(contactoEmail);
      contactosRepository.insert(contactoEmail);
    }

    String telegram = context.formParam("TelegramContacto");
    if (telegram != null && !telegram.isBlank()) {
      Contacto contactoTelegram = new Telegram(usuario, telegram);
      contactos.add(contactoTelegram);
      contactosRepository.insert(contactoTelegram);
    }

    CoordenadasGeograficas coordenadasGeograficas = new CoordenadasGeograficas(Double.parseDouble(context.formParam("latitud")), Double.parseDouble(context.formParam("longitud")));
    AreaGeografica areaGeografica = new AreaGeografica(coordenadasGeograficas, Float.parseFloat(context.formParam("radio")));

    String cuil = context.formParam("cuil");

    Tecnico tecnico = new Tecnico(documento, context.formParam("nombre"), context.formParam("apellido"), LocalDate.parse(context.formParam("dob")), cuil, areaGeografica, context.formParam("password"), rolTecnico);


    new TecnicoRepository().insert(tecnico);

    context.redirect("/quiero-ayudar");
  }
}


