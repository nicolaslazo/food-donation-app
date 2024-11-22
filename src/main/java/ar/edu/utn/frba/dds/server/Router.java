package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controllers.PersonaVulnerableController;
import ar.edu.utn.frba.dds.controllers.cargacsv.CargaCSVController;
import ar.edu.utn.frba.dds.controllers.colaborador.ColaboradorController;
import ar.edu.utn.frba.dds.controllers.contacto.ContactoController;
import ar.edu.utn.frba.dds.controllers.contribucion.AgregarRecompensasController;
import ar.edu.utn.frba.dds.controllers.contribucion.CuidadoHeladeraController;
import ar.edu.utn.frba.dds.controllers.contribucion.DistribuirViandaController;
import ar.edu.utn.frba.dds.controllers.contribucion.DonacionDineroController;
import ar.edu.utn.frba.dds.controllers.contribucion.DonacionViandaController;
import ar.edu.utn.frba.dds.controllers.contribucion.EntregaTarjetasController;
import ar.edu.utn.frba.dds.controllers.formascolaboracion.FormasColaboracionController;
import ar.edu.utn.frba.dds.controllers.heladera.incidente.VisitaTecnicaController;
import ar.edu.utn.frba.dds.controllers.heladera.incidente.IncidenteController;
import ar.edu.utn.frba.dds.controllers.home.HomeController;
import ar.edu.utn.frba.dds.controllers.quienessomos.QuienesSomosController;
import ar.edu.utn.frba.dds.controllers.quieroayudar.QuieroAyudarController;
import ar.edu.utn.frba.dds.controllers.session.SessionController;
import ar.edu.utn.frba.dds.controllers.tecnico.TecnicoController;
import ar.edu.utn.frba.dds.controllers.terminosycondiciones.TerminosYCondicionesController;
import ar.edu.utn.frba.dds.controllers.verreportes.PDFGeneratorController;
import ar.edu.utn.frba.dds.models.entities.users.Permiso;
import ar.edu.utn.frba.dds.models.entities.users.Rol;
import ar.edu.utn.frba.dds.models.repositories.users.PermisosRepository;
import ar.edu.utn.frba.dds.models.repositories.users.RolesRepository;
import ar.edu.utn.frba.dds.server.middleware.AuthMiddleware;
import io.javalin.Javalin;

import java.util.Arrays;
import java.util.Set;

public class Router {

  public static void init(Javalin app) {
    PermisosRepository permisosRepository = new PermisosRepository();
    Permiso permisoAsignarTarjetas = permisosRepository.findByName("Asignar-Tarjetas").get();
    Permiso permisoVerReportes = permisosRepository.findByName("Ver-Reportes").get();
    Permiso permisoCrearRecompensa = permisosRepository.findByName("Crear-Recompensas").get();
    Permiso permisoCrearTecnico = permisosRepository.findByName("Crear-Tecnico").get();
    Permiso permisoCuidarHeladera = permisosRepository.findByName("Cuidar-Heladera").get();
    Permiso permisoDonarDinero = permisosRepository.findByName("Donar-Dinero").get();
    Permiso permisoSolicitarTarjetas = permisosRepository.findByName("Solicitar-Tarjetas").get();
    Permiso permisoDonarViandas = permisosRepository.findByName("Donar-Viandas").get();
    Permiso permisoDistribuirViandas = permisosRepository.findByName("Distribuir-Viandas").get();
    Permiso permisoCargarVisitaTecnica = permisosRepository.findByName("Cargar-Visita-Tecnica").get();

    RolesRepository repositorioRoles = new RolesRepository();
    Rol rolColaboradorFisico = repositorioRoles.findByName("COLABORADORFISICO").get();
    Rol rolColaboradorJuridico = repositorioRoles.findByName("COLABORADORJURIDICO").get();
    Set<Rol> rolesColaboradores = Set.of(rolColaboradorFisico, rolColaboradorJuridico);
    Set<Rol> rolesPersonaVulnerable = Set.of(repositorioRoles.findByName("PERSONAVULNERABLE").get());

    app.before(SessionController::sessionInfo);

    app.get("/colaborador/login", new SessionController("logueo/login/logincolaborador.hbs")::index);
    app.post(
        "/colaborador/login",
        new SessionController("logueo/login/logincolaborador.hbs", rolesColaboradores)::create);

    app.post("/colaborador/logout", SessionController::delete);
    app.get("/colaborador/registro", new ColaboradorController()::index);
    app.post("/colaborador/registro", new ColaboradorController()::create);

    app.get(
        "/persona-vulnerable/login",
        new SessionController("logueo/login/loginpersonavulnerable.hbs")::index);
    app.post(
        "/persona-vulnerable/login",
        new SessionController("logueo/login/loginpersonavulnerable.hbs", rolesPersonaVulnerable)::create);

    // Terminos
    app.get("/terminos", new TerminosYCondicionesController()::index);

    // Rutas de la Navbar
    app.get("/", new HomeController()::index);
    app.get("/quienes-somos", new QuienesSomosController()::index);
    app.get("/contacto", new ContactoController()::index);
    app.post("/contacto", new ContactoController()::create);

    // --- Rutas Protegidas que requieren autenticación ---
    // Incidente/*
    app.before("incidentes/*", new AuthMiddleware());
    app.get("/incidentes/reportar-falla", IncidenteController.getInstancia()::index);
    app.get("/incidentes/cargar-visita-tecnica", new VisitaTecnicaController()::index, permisoCargarVisitaTecnica);

    //Tecnico
    app.before("/tecnico/crear", new AuthMiddleware());
    app.get("/tecnico/crear", new TecnicoController()::index, permisoCrearTecnico);

    // Contribucion/*
    app.before("/contribucion/*", new AuthMiddleware());
    app.get("/contribucion/cuidado-heladera", new CuidadoHeladeraController()::index, permisoCuidarHeladera);
    app.post("/contribucion/cuidado-heladera", new CuidadoHeladeraController()::create, permisoCuidarHeladera);
    app.get("/contribucion/donacion-dinero", new DonacionDineroController()::index, permisoDonarDinero);
    app.post("/contribucion/donacion-dinero", new DonacionDineroController()::create, permisoDonarDinero);
    app.get("/contribucion/entrega-tarjetas", new EntregaTarjetasController()::index, permisoSolicitarTarjetas);
    app.post("/contribucion/entrega-tarjetas", new EntregaTarjetasController()::create, permisoSolicitarTarjetas);
    app.get("/contribucion/agregar-recompensa", new AgregarRecompensasController()::index, permisoCrearRecompensa);
    app.get("/contribucion/donacion-vianda", new DonacionViandaController()::index, permisoDonarViandas);
    app.post("/contribucion/donacion-vianda", new DonacionViandaController()::create, permisoDonarViandas);
    app.get("/contribucion/redistribucion-vianda", new DistribuirViandaController()::index, permisoDistribuirViandas);
    app.post("/contribucion/redistribucion-vianda", new DistribuirViandaController()::create, permisoDistribuirViandas);

    // Registro Persona Vulnerable
    app.before("/persona-vulnerable/registro", new AuthMiddleware());
    app.get("/persona-vulnerable/registro", new PersonaVulnerableController()::index, permisoAsignarTarjetas);
    app.post("/persona-vulnerable/registro", new PersonaVulnerableController()::create, permisoAsignarTarjetas);

    // Carga CSV
    app.before("/carga-csv", new AuthMiddleware());
    app.get("/carga-csv", new CargaCSVController()::index);

    //Reportes
    app.before("/ver-reportes", new AuthMiddleware());
    app.get("/ver-reportes", new PDFGeneratorController()::index, permisoVerReportes);

    // Quiero Ayudar & Formas de Colaboración
    app.before("/quiero-ayudar", new AuthMiddleware());
    app.get("/quiero-ayudar", new QuieroAyudarController()::index);
    app.before("/formas-colaboracion", new AuthMiddleware());
    app.get("/formas-colaboracion", new FormasColaboracionController()::index);

    app.exception(Exception.class, (e, ctx) -> {
      ctx.status(500);
      ctx.result("Internal server error: " + e.getMessage() + "\n\n" + Arrays.toString(e.getStackTrace()));
    });
  }
}