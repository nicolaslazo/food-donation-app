package ar.edu.utn.frba.dds.controllers.terminosycondiciones;

import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public class TerminosYCondicionesController {
  public void index(Context context) {
    Map<String, Object> model = context.attribute("model");

    context.render("terminos/terminos.hbs", model);
  }
}
