package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controllers.PersonaVulnerableController;
import ar.edu.utn.frba.dds.controllers.cargacsv.CargaCSVController;
import ar.edu.utn.frba.dds.controllers.colaborador.ColaboradorController;
import ar.edu.utn.frba.dds.controllers.contribucion.DonacionDineroController;
import ar.edu.utn.frba.dds.controllers.formascolaboracion.FormasColaboracionController;
import ar.edu.utn.frba.dds.controllers.heladera.incidente.IncidenteController;
import ar.edu.utn.frba.dds.controllers.home.HomeController;
import ar.edu.utn.frba.dds.controllers.quieroayudar.QuieroAyudarController;
import ar.edu.utn.frba.dds.controllers.session.SessionController;
import ar.edu.utn.frba.dds.controllers.terminosycondiciones.TerminosYCondicionesController;
import ar.edu.utn.frba.dds.models.entities.users.Permiso;
import ar.edu.utn.frba.dds.models.repositories.users.PermisosRepository;
import ar.edu.utn.frba.dds.server.middleware.AuthMiddleware;
import io.javalin.Javalin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Router {

  public static void init(Javalin app) {
    PermisosRepository permisosRepository = new PermisosRepository();
    Permiso permisoAsignarTarjetas = permisosRepository.findByName("Asignar-Tarjetas").get();

    app.before(ctx -> new SessionController().sessionInfo(ctx));

    // --- Ruta Públicas ---
    app.get("/colaborador/login", new SessionController()::index);
    app.post("/colaborador/login", new SessionController()::create);
    app.post("/colaborador/logout", new SessionController()::delete);

    app.get("/", new HomeController()::index);
    app.get("/terminos-y-condiciones", new TerminosYCondicionesController()::index);
    app.get("/quiero-ayudar", new QuieroAyudarController()::index);
    app.get("/formas-colaboracion", new FormasColaboracionController()::index);

    app.get("/colaborador/registro", new ColaboradorController()::index);
    app.post("/colaborador/registro", new ColaboradorController()::create);

    // --- Rutas Protegidas que requieren autenticación ---
    // Incidente/*
    app.before("incidentes/*", new AuthMiddleware());
    app.get("/incidentes/reportar-falla", IncidenteController.getInstancia()::index);

    // Contribucion/*
    app.before("/contribucion/*", new AuthMiddleware());
    app.get("/contribucion/donacion-dinero", new DonacionDineroController()::index);

    // Registro Persona Vulnerable
    app.before("/persona-vulnerable/registro", new AuthMiddleware());
    app.get("/persona-vulnerable/registro", new PersonaVulnerableController()::index, permisoAsignarTarjetas);
    app.post("/persona-vulnerable/registro", new PersonaVulnerableController()::create, permisoAsignarTarjetas);

    // Carga CSV
    app.before("/carga-csv", new AuthMiddleware());
    app.get("/carga-csv", new CargaCSVController()::index);

    app.exception(Exception.class, (e, ctx) -> {
      ctx.status(500);
      ctx.result("Internal server error: " + e.getMessage() + "\n\n" + Arrays.toString(e.getStackTrace()));
    });
  }
}