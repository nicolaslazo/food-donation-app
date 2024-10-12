package ar.edu.utn.frba.dds.controllers.contribucion;

import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public class DonacionDineroController {
  public void index(Context context) {
    Map<String, Object> model = new HashMap<>();

    // Verifica si el usuario está autenticado
    model.put("usuarioAutenticado", context.sessionAttribute("usuarioAutenticado"));

    context.render("contribuciones/donacion_dinero/donacion_dinero.hbs", model);
  }
}
