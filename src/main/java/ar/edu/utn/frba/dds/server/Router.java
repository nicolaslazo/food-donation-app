package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controllers.PersonaVulnerableController;
import ar.edu.utn.frba.dds.controllers.formascolaboracion.FormasColaboracionController;
import ar.edu.utn.frba.dds.controllers.home.HomeController;
import ar.edu.utn.frba.dds.controllers.quieroayudar.QuieroAyudarController;
import ar.edu.utn.frba.dds.controllers.terminosycondiciones.TerminosYCondicionesController;
import io.javalin.Javalin;

import java.util.Arrays;

public class Router {

  public static void init(Javalin app) {
    app.get("/prueba", ctx -> ctx.result("Hola mundo!"));

    app.get("/", new HomeController()::index);
    app.get("/terminos-y-condiciones", new TerminosYCondicionesController()::index);
    app.get("/quiero-ayudar", new QuieroAyudarController()::index);
    app.get("/formas-colaboracion", new FormasColaboracionController()::index);

    app.get("/carga-persona-vulnerable", new PersonaVulnerableController()::index);

    app.exception(Exception.class, (e, ctx) -> {
      ctx.status(500);
      ctx.result("Internal server error: " + Arrays.toString(e.getStackTrace()));
    });
  }
}