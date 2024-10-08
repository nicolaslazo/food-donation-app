package ar.edu.utn.frba.dds.controllers.session;

import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.users.UsuariosRepository;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public class SessionController {
  public void index(Context context) {
    context.render("logueo/login/login.hbs");
  }

  public void create(Context context) {
    try {
      Usuario usuario = new UsuariosRepository().findByEmailAndPassword(
              context.formParam("nombre"),
              context.formParam("password")
      );
      context.sessionAttribute("user_id", usuario.getId());
      context.redirect("/");

    } catch (Exception e) {
      Map<String, Object> modelo = new HashMap<>();
      e.printStackTrace();
      modelo.put("error", "usuario o contraseña invalidas");
      context.render("logueo/login/login.hbs", modelo);
    }
  }
}
