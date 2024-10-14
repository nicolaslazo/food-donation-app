package ar.edu.utn.frba.dds.controllers.session;

import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.users.PermisosRepository;
import ar.edu.utn.frba.dds.models.repositories.users.UsuariosRepository;
import io.javalin.http.Context;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.Map;

public class SessionController {
  public void index(Context context) {
    Map<String, Object> model = context.attribute("model");

    context.render("logueo/login/login.hbs", model);
  }

  public void create(Context context) {
    try {
      Usuario usuario = new UsuariosRepository().findByEmailAndPassword(
          context.formParam("email"),
          DigestUtils.sha256Hex(context.formParam("password"))
      );
      // Recupero el model y Setteo el estado
      Map<String, Object> model = context.attribute("model");
      model.put("usuarioAutenticado", true);
      context.attribute("model", model);

      // Asigno los SessionAttribute
      context.sessionAttribute("user_id", usuario.getId());
      context.sessionAttribute("usuarioAutenticado", true);
      context.sessionAttribute("permisos", new PermisosRepository().findAll(usuario).toList());
      context.redirect("/");

    } catch (Exception e) {
      Map<String, Object> model = context.attribute("model");
      model.put("error", "usuario o contraseña invalidas");
      model.put("usuarioAutenticado", false);
      context.render("logueo/login/login.hbs", model);
    }
  }

  public void delete(Context context) {
    // Invalidamos la sesión
    Map<String, Object> model = context.attribute("model");
    model.put("usuarioAutenticado", false);
    context.attribute("model", model);
    context.sessionAttribute("user_id", null);
    context.sessionAttribute("usuarioAutenticado", false);
    context.sessionAttribute("permisos", null);
    context.redirect("/");
  }

  public void sessionInfo(Context context) {
    // Inicializo el Model
    Map<String, Object> model = context.attribute("model");
    if (model == null) {
      model = new HashMap<>();
    }

    // Verifico el estado de la autenticación
    Boolean usuarioAutenticado = (Boolean) context.sessionAttribute("usuarioAutenticado");
    if (usuarioAutenticado == null) {
      usuarioAutenticado = false;
    }

    // Almaceno el estado de la autenticación
    context.sessionAttribute("usuarioAutenticado", usuarioAutenticado);
    model.put("usuarioAutenticado", usuarioAutenticado);
    context.attribute("model", model);
  }
}
