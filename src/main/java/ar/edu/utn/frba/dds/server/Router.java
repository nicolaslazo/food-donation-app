package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controllers.home.HomeController;
import ar.edu.utn.frba.dds.controllers.terminosycondiciones.TerminosYCondicionesController;
import io.javalin.Javalin;

public class Router {

  public static void init(Javalin app) {
    app.get("/prueba", ctx -> ctx.result("Hola mundo!"));

    app.get("/", new HomeController()::index);
    app.get("/terminos-y-condiciones", new TerminosYCondicionesController()::index);
  }
}