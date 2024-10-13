package ar.edu.utn.frba.dds.controllers.home;

import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public class HomeController {
  public void index(Context context) {
    Map<String, Object> model = context.attribute("model");

    context.render("home/home.hbs", model);
  }
}
