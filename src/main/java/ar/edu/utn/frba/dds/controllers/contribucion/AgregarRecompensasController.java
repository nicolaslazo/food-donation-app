package ar.edu.utn.frba.dds.controllers.contribucion;

import io.javalin.http.Context;

import java.util.Map;

public class AgregarRecompensasController {
  public void index(Context context) {
    Map<String, Object> model = context.attribute("model");
    context.render("contribuciones/agregar_recompensa/agregar_recompensa.hbs", model);
  }
}