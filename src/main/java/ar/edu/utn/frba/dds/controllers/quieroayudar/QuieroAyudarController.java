package ar.edu.utn.frba.dds.controllers.quieroayudar;

import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public class QuieroAyudarController {
  public void index(Context context) {
    Map<String, Object> model = context.attribute("model");

    context.render("quieroayudar/quieroayudar.hbs", model);
  }
}
