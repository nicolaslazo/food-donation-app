package ar.edu.utn.frba.dds.controllers.home;

import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public class HomeController {
  public void index(Context context) {
    Map<String, Object> model = new HashMap<>();

    // Verifica si el usuario está autenticado
    model.put("usuarioAutenticado", context.sessionAttribute("usuarioAutenticado"));
    context.render("home/home.hbs", model);
  }
}
