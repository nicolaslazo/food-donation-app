package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controllers.cargacsv.CargaCSVController;
import ar.edu.utn.frba.dds.controllers.PersonaVulnerableController;
import ar.edu.utn.frba.dds.controllers.heladera.incidente.IncidenteController;
import ar.edu.utn.frba.dds.controllers.heladera.incidente.IncidenteController;
import ar.edu.utn.frba.dds.controllers.heladera.incidente.IncidenteController;
import ar.edu.utn.frba.dds.controllers.colaborador.ColaboradorController;
import ar.edu.utn.frba.dds.controllers.contribucion.DonacionDineroController;
import ar.edu.utn.frba.dds.controllers.formascolaboracion.FormasColaboracionController;
import ar.edu.utn.frba.dds.controllers.home.HomeController;
import ar.edu.utn.frba.dds.controllers.quieroayudar.QuieroAyudarController;
import ar.edu.utn.frba.dds.controllers.session.SessionController;
import ar.edu.utn.frba.dds.controllers.terminosycondiciones.TerminosYCondicionesController;
import io.javalin.Javalin;

import java.util.Arrays;

public class Router {

  public static void init(Javalin app) {
    app.get("/prueba", ctx -> ctx.result("Hola mundo!"));

    app.get("/colaborador/login", new SessionController()::index);
    app.post("/colaborador/login", new SessionController()::create);

    app.get("/", new HomeController()::index);
    app.get("/terminos-y-condiciones", new TerminosYCondicionesController()::index);
    app.get("/quiero-ayudar", new QuieroAyudarController()::index);
    app.get("/carga-csv", new CargaCSVController()::index);
    app.get("/formas-colaboracion", new FormasColaboracionController()::index);

    app.get("/colaborador/registro", new ColaboradorController()::index);
    app.post("/colaborador/registro", new ColaboradorController()::create);
    app.get("/carga-persona-vulnerable", new PersonaVulnerableController()::index);

    app.get("/incidentes/reportar-falla", IncidenteController.getInstancia()::index);

    app.get("/incidentes/reportar-falla", IncidenteController.getInstancia()::index);

    app.get("/incidentes/reportar-falla", IncidenteController.getInstancia()::index);

    app.get("/contribuciones/donacion-dinero", new DonacionDineroController()::index);

    app.exception(Exception.class, (e, ctx) -> {
      ctx.status(500);
      ctx.result("Internal server error: " + e.getMessage() + "\n\n" + Arrays.toString(e.getStackTrace()));
    });
  }
}