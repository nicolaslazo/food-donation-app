package ar.edu.utn.frba.dds.controllers.home;

import io.javalin.http.Context;

public class HomeController {
  public void index(Context context) {
    context.render("home/home.hbs");
  }
}
