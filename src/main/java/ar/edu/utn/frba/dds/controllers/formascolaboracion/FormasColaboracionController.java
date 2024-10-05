package ar.edu.utn.frba.dds.controllers.formascolaboracion;

import io.javalin.http.Context;

public class FormasColaboracionController {
  public void index(Context context) {
    context.render("formascolaboracion/formascolaboracion.hbs");
  }
}
