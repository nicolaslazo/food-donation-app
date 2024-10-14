package ar.edu.utn.frba.dds.controllers.quienessomos;

import io.javalin.http.Context;

public class QuienesSomosController {
  public void index(Context context) {
    context.render("quienessomos/quienes_somos.hbs");
  }
}
