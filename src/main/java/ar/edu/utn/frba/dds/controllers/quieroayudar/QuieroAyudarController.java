package ar.edu.utn.frba.dds.controllers.quieroayudar;

import io.javalin.http.Context;

public class QuieroAyudarController {
  public void index(Context context) {
    context.render("quieroayudar/quieroayudar.hbs");
  }
}
