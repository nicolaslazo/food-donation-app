package ar.edu.utn.frba.dds.controllers.session;

import ar.edu.utn.frba.dds.models.entities.users.PermisoDenegadoException;
import ar.edu.utn.frba.dds.models.entities.users.Rol;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.users.PermisosRepository;
import ar.edu.utn.frba.dds.models.repositories.users.RolesRepository;
import ar.edu.utn.frba.dds.models.repositories.users.UsuariosRepository;
import io.javalin.http.Context;
import lombok.NonNull;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SessionController {
  @NonNull String pathTemplate;
  Set<Rol> rolesAceptados;

  public SessionController(@NonNull String pathTemplate, Set<Rol> rolesAceptados) {
    Rol rolAdmin = new RolesRepository().findByName("ADMINISTRADOR").get();

    this.pathTemplate = pathTemplate;
    this.rolesAceptados = new HashSet<>(rolesAceptados);
    this.rolesAceptados.add(rolAdmin);  // Los admins pueden hacer todo
  }

  public SessionController(@NonNull String pathTemplate) {
    this.pathTemplate = pathTemplate;
  }

  public static void delete(Context context) {
    // Invalidamos la sesión
    Map<String, Object> model = context.attribute("model");
    model.put("usuarioAutenticado", false);
    context.attribute("model", model);
    context.sessionAttribute("user_id", null);
    context.sessionAttribute("usuarioAutenticado", false);
    context.sessionAttribute("permisos", null);
    context.sessionAttribute("esPersonaVulnerable", false);
    context.redirect("/");
  }

  public static void sessionInfo(Context context) {
    // Inicializo el Model
    Map<String, Object> model = context.attribute("model");
    if (model == null) {
      model = new HashMap<>();
    }

    // Verifico el estado de la autenticación
    Boolean usuarioAutenticado = context.sessionAttribute("usuarioAutenticado");
    if (usuarioAutenticado == null) {
      usuarioAutenticado = false;
    }

    Boolean esPersonaVulnerable = context.sessionAttribute("esPersonaVulnerable");
    if (esPersonaVulnerable == null) {
      esPersonaVulnerable = false;
    }

    // Almaceno el estado de la autenticación
    context.sessionAttribute("usuarioAutenticado", usuarioAutenticado);
    context.sessionAttribute("esPersonaVulnerable", esPersonaVulnerable);
    model.put("usuarioAutenticado", usuarioAutenticado);
    model.put("esPersonaVulnerable", esPersonaVulnerable);
    context.attribute("model", model);
  }

  public void index(Context context) {
    // Verifica si el usuario ya está autenticado
    Boolean usuarioAutenticado = context.sessionAttribute("usuarioAutenticado");
    if (usuarioAutenticado) {
      // Si ya tiene sesión activa, redirigir o mostrar mensaje
      context.redirect("/");  // Redirige al inicio o a otra página
      return;
    }

    Map<String, Object> model = context.attribute("model");

    context.render(pathTemplate, model);
  }

  public void create(Context context) {
    try {
      if (rolesAceptados == null)
        throw new PermisoDenegadoException("No se puede crear una sesión sin un rol que la justifique");

      Usuario usuario = new UsuariosRepository().findByEmailAndPassword(
          context.formParam("email"),
          DigestUtils.sha256Hex(context.formParam("password"))
      );

      // Aseguro que el usuario tiene al menos uno de los roles aceptados
      if (usuario.getRoles().stream().noneMatch(rolesAceptados::contains))
        throw new PermisoDenegadoException("El usuario no tiene ninguno de los roles aceptables");


      if (!usuario.getActivo()) {
        context.json(Map.of(
            "message", "Su usuario fue desactivado. Puede usar la pestaña de contacto para resolver el problema",
            "success", false
        ));
        return;
      }

      // Recupero el model y Setteo el estado
      Map<String, Object> model = context.attribute("model");
      model.put("usuarioAutenticado", true);
      context.attribute("model", model);

      // Asigno los SessionAttribute
      context.sessionAttribute("user_id", usuario.getId());
      context.sessionAttribute("usuarioAutenticado", true);
      context.sessionAttribute("esPersonaVulnerable", false);

      // Le metemos los permisos de todos los roles que tiene,
      // independientemente de si pertenecen a alguno de los rolesAceptados.
      // De esta manera nos ahorramos tener que reloguearnos para hacer operaciones distintas
      context.sessionAttribute("permisos", new PermisosRepository().findAll(usuario).toList());

      // Si tiene rol de Persona Vulnerable, puede hacer una solicitud de viandas
      // tambien se lo redirije directamente al mapa
      if (usuario.tienePermiso("Abrir-Heladera-Consumicion")) {
        context.sessionAttribute("abrirHeladeraConsumicion", true);
        if (usuario.getRoles().contains(new Rol("PERSONAVULNERABLE"))) {
          context.sessionAttribute("esPersonaVulnerable", true);
        }
      }

      if (context.sessionAttribute("esPersonaVulnerable")) {
        context.json(Map.of(
                "message", "Cuenta logueada con éxito! Redirigiendo en tres segundos...",
                "success", true,
                "urlRedireccion", "/mapa",
                "demoraRedireccionEnSegundos", 3
        ));
      } else {
        // Si no es persona vulnerable, sigue el flujo normal
        context.json(Map.of(
                "message", "Cuenta logueada con éxito! Redirigiendo en tres segundos...",
                "success", true,
                "urlRedireccion", "/",
                "demoraRedireccionEnSegundos", 3
        ));
      }
    } catch (Exception e) {
      context.json(Map.of(
          "message", "Contraseña incorrecta o usuario no encontrado",
          "success", false
      ));
    }
  }
}
