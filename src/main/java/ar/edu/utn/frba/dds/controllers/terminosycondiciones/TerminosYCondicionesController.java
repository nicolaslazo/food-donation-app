package ar.edu.utn.frba.dds.controllers.terminosycondiciones;

import io.javalin.http.Context;

public class TerminosYCondicionesController {
  public void index(Context context) {
    System.out.println("Intento de renderizar");
    try {
      context.render("terminos/terminos.hbs");
    } catch (Exception e) {
      e.printStackTrace();
      context.status(500).result("Error: " + e.getMessage());
    }
  }
}
