package ar.edu.utn.frba.dds.controllers.formascolaboracion;

import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public class FormasColaboracionController {
  public void index(Context context) {
    Map<String, Object> model = context.attribute("model");

    context.render("formascolaboracion/formascolaboracion.hbs", model);
  }
}
