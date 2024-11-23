package ar.edu.utn.frba.dds.controllers.tecnico;

import ar.edu.utn.frba.dds.dtos.input.tecnico.TecnicoInputDTO;
import ar.edu.utn.frba.dds.models.entities.Tecnico;
import ar.edu.utn.frba.dds.models.entities.contacto.Contacto;
import ar.edu.utn.frba.dds.models.entities.contacto.Email;
import ar.edu.utn.frba.dds.models.entities.contacto.Telegram;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.AreaGeografica;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.entities.users.PermisoDenegadoException;
import ar.edu.utn.frba.dds.models.entities.users.Rol;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.TecnicoRepository;
import ar.edu.utn.frba.dds.models.repositories.contacto.ContactosRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.users.PermisosRepository;
import ar.edu.utn.frba.dds.models.repositories.users.RolesRepository;
import ar.edu.utn.frba.dds.models.repositories.users.UsuariosRepository;
import io.javalin.http.Context;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


public class TecnicoController {
  TecnicoRepository tecnicoRepository = new TecnicoRepository();
  PermisosRepository permisosRepository = new PermisosRepository();
  RolesRepository rolesRepository = new RolesRepository();

  public void crearTecnicoJson(TecnicoInputDTO data, Usuario usuarioCreador) throws PermisoDenegadoException {
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

    // Recupero heladeras
    HeladerasRepository heladerasRepository = new HeladerasRepository();
    List<Heladera> heladeras = heladerasRepository.findAll().toList();

    // Tomo información de las heladeras.
    List<Map<String, Object>> heladerasData = heladeras.stream().map(heladera -> {
      Map<String, Object> heladeraData = new HashMap<>();
      heladeraData.put("idHeladera", heladera.getId());
      heladeraData.put("lat", heladera.getUbicacion().getLatitud());
      heladeraData.put("long", heladera.getUbicacion().getLongitud());
      heladeraData.put("nombre", heladera.getNombre());
      heladeraData.put("capacidadDisponible", heladerasRepository.getCapacidadDisponible(heladera));
      return heladeraData;
    }).collect(Collectors.toList());

    model.put("heladeras", heladerasData);

    context.render("agregartecnico/agregartecnico.hbs", model);
  }

  public void create(Context context) throws PermisoDenegadoException {

    Usuario creador = new UsuariosRepository().findById(context.sessionAttribute("user_id")).get();
    creador.assertTienePermiso(
        "Crear-Tecnico",
        "No tienes permisos para crear un nuevo tecnico"
    );
    String email = context.formParam("email");

    Optional<Usuario> usuarioExistente = Optional.ofNullable(new UsuariosRepository().findByEmail(email));
    if (usuarioExistente.isPresent()) {
      context.status(400); // Código de error para indicar un problema
      context.result("El correo ya está registrado.");
      return;
    }

    Documento documento = crearDocumento(context);
    Usuario usuario = crearUsuario(context, documento);
    new UsuariosRepository().insert(usuario);
    List<Contacto> contactos = crearContactos(context, usuario);
    insertContactos(contactos);
    Tecnico tecnico = createTecnico(context, documento);
    new TecnicoRepository().insert(tecnico);
    context.redirect("/quiero-ayudar");
  }

  private Documento crearDocumento(Context context) {
    return new Documento(
        TipoDocumento.fromString(context.formParam("documento")),
        Integer.parseInt(context.formParam("numeroDocumento"))
    );
  }

  private Usuario crearUsuario(Context context, Documento documento) {
    Rol rolTecnico = new RolesRepository().findByName("TECNICO")
        .orElseThrow(() -> new IllegalArgumentException("Rol 'TECNICO' no encontrado."));
    return new Usuario(
        documento,
        context.formParam("nombre"),
        context.formParam("apellido"),
        LocalDate.parse(context.formParam("dob")),
        DigestUtils.sha256Hex(context.formParam("password")),
        new HashSet<>(List.of(rolTecnico))
    );
  }


  private List<Contacto> crearContactos(Context context, Usuario usuario) {
    List<Contacto> contactos = new ArrayList<>();
    String email = context.formParam("email");
    if (email != null && !email.isBlank()) {
      Contacto contactoEmail = new Email(usuario, email);
      contactos.add(contactoEmail);
    }
    String telegram = context.formParam("TelegramContacto");
    if (telegram != null && !telegram.isBlank()) {
      Contacto contactoTelegram = new Telegram(usuario, telegram);
      contactos.add(contactoTelegram);
    }
    return contactos;
  }

  private void insertContactos(List<Contacto> contactos) {
    ContactosRepository contactosRepository = new ContactosRepository();
    for (Contacto contacto : contactos) {
      contactosRepository.insert(contacto);
    }
  }

  private Tecnico createTecnico(Context context, Documento documento) {
    CoordenadasGeograficas coordenadasGeograficas = new CoordenadasGeograficas(
        Double.parseDouble(context.formParam("latitud")),
        Double.parseDouble(context.formParam("longitud"))
    );
    AreaGeografica areaGeografica = new AreaGeografica(coordenadasGeograficas, Float.parseFloat(context.formParam("radio")));
    String cuil = context.formParam("cuil");
    Rol rolTecnico = new RolesRepository().findByName("TECNICO")
        .orElseThrow(() -> new IllegalArgumentException("Rol 'TECNICO' no encontrado."));

    return new Tecnico(
        documento,
        context.formParam("nombre"),
        context.formParam("apellido"),
        LocalDate.parse(context.formParam("dob")),
        cuil,
        areaGeografica,
        context.formParam("password"),
        rolTecnico
    );
  }
}



