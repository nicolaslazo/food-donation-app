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
    context.render("logueo/login/login.hbs");
  }

  public void create(Context context) {
    try {
      Usuario usuario = new UsuariosRepository().findByEmailAndPassword(
          context.formParam("email"),
          DigestUtils.sha256Hex(context.formParam("password"))
      );
      context.sessionAttribute("user_id", usuario.getId());
      context.sessionAttribute("usuarioAutenticado", true);
      context.sessionAttribute("permisos", new PermisosRepository().findAll(usuario).toList());
      context.redirect("/");

    } catch (Exception e) {
      Map<String, Object> modelo = new HashMap<>();
      modelo.put("error", "usuario o contraseña invalidas");
      context.render("logueo/login/login.hbs", modelo);
    }
  }

  public void delete(Context context) {
    // Invalidamos la sesión
    context.sessionAttribute("user_id", null);
    context.sessionAttribute("usuarioAutenticado", false);
    context.sessionAttribute("permisos", null);
    context.redirect("/");
  }

  public void sessionInfo(Context context) {
    Boolean usuarioAutenticado = (Boolean) context.sessionAttribute("usuarioAutenticado");
    if (usuarioAutenticado == null) {
      usuarioAutenticado = false;
    }
    context.sessionAttribute("usuarioAutenticado", usuarioAutenticado);
    System.out.println("Usuario Autenticado?: " + usuarioAutenticado);
  }
}
