package ar.edu.utn.frba.dds.controllers.terminosycondiciones;

import io.javalin.http.Context;

public class TerminosYCondicionesController {
  public void index(Context context) {
    context.render("terminos/terminos.hbs");
  }
}
