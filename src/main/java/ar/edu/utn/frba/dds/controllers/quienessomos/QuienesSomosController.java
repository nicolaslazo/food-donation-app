package ar.edu.utn.frba.dds.controllers.quienessomos;

import io.javalin.http.Context;

import java.util.Map;

public class QuienesSomosController {
  public void index(Context context) {
    Map<String, Object> model = context.attribute("model");

    context.render("quienessomos/quienes_somos.hbs", model);
  }
}
