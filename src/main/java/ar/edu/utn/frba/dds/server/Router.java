package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.controllers.PersonaVulnerableController;
import ar.edu.utn.frba.dds.controllers.cargacsv.CargaCSVController;
import ar.edu.utn.frba.dds.controllers.colaborador.ColaboradorController;
import ar.edu.utn.frba.dds.controllers.contribucion.CuidadoHeladeraController;
import ar.edu.utn.frba.dds.controllers.contribucion.DonacionDineroController;
import ar.edu.utn.frba.dds.controllers.contribucion.EntregaTarjetasController;
import ar.edu.utn.frba.dds.controllers.formascolaboracion.FormasColaboracionController;
import ar.edu.utn.frba.dds.controllers.heladera.incidente.IncidenteController;
import ar.edu.utn.frba.dds.controllers.home.HomeController;
import ar.edu.utn.frba.dds.controllers.quieroayudar.QuieroAyudarController;
import ar.edu.utn.frba.dds.controllers.session.SessionController;
import ar.edu.utn.frba.dds.controllers.terminosycondiciones.TerminosYCondicionesController;
import ar.edu.utn.frba.dds.controllers.tienda.TiendaController;
import ar.edu.utn.frba.dds.models.entities.users.Permiso;
import ar.edu.utn.frba.dds.models.repositories.users.PermisosRepository;
import io.javalin.Javalin;

import java.util.Arrays;

public class Router {

  public static void init(Javalin app) {
    PermisosRepository permisosRepository = new PermisosRepository();
    Permiso permisoAsignarTarjetas = permisosRepository.findByName("Asignar-Tarjetas").get();
    Permiso permisoCuidarHeladera = permisosRepository.findByName("Cuidar-Heladera").get();
    Permiso permisoAdministrarProductos = permisosRepository.findByName("Administrar-Productos-Servicios").get();
    Permiso permisoCanjearProductos = permisosRepository.findByName("CanjearProductos").get();
    Permiso permisoDonarDinero = permisosRepository.findByName("Donar-Dinero").get();
    Permiso permisoSolicitarTarjetas = permisosRepository.findByName("Solicitar-Tarjetas").get();

    app.get("/prueba", ctx -> ctx.result("Hola mundo!"));

    app.get("/colaborador/login", new SessionController()::index);
    app.post("/colaborador/login", new SessionController()::create);

    app.get("/", new HomeController()::index);
    
    app.get("/tienda", new TiendaController()::index);    
    app.post("/tienda/ofrecerProducto/createProducto", new TiendaController()::createProducto, permisoAdministrarProductos);
    app.delete("/tienda/ofrecerProducto/deleteProducto/{id}", new TiendaController()::deleteProducto, permisoAdministrarProductos);
    app.post("/tienda/ofrecerProducto/{id}", new TiendaController()::modifyProducto, permisoAdministrarProductos);
    app.get("/tienda/ofrecerProducto", new TiendaController()::ofrecerProducto, permisoAdministrarProductos);
    app.get("/tienda/canjearProductos", new TiendaController()::canjearProductos, permisoCanjearProductos);
    app.post("/tienda/canjearProductos/{id}", new TiendaController()::canjearProductosPost, permisoCanjearProductos);

    app.get("/terminos-y-condiciones", new TerminosYCondicionesController()::index);
    app.get("/quiero-ayudar", new QuieroAyudarController()::index);
    app.get("/carga-csv", new CargaCSVController()::index);
    app.get("/formas-colaboracion", new FormasColaboracionController()::index);

    app.get("/colaborador/registro", new ColaboradorController()::index);
    app.post("/colaborador/registro", new ColaboradorController()::create);
    app.get("/persona-vulnerable/registro", new PersonaVulnerableController()::index, permisoAsignarTarjetas);
    app.post("/persona-vulnerable/registro", new PersonaVulnerableController()::create, permisoAsignarTarjetas);

    app.get("/incidentes/reportar-falla", IncidenteController.getInstancia()::index);

    app.get("/contribucion/cuidado-heladera", new CuidadoHeladeraController()::index, permisoCuidarHeladera);
    app.post("/contribucion/cuidado-heladera", new CuidadoHeladeraController()::create, permisoCuidarHeladera);
    app.get("/contribucion/donacion-dinero", new DonacionDineroController()::index, permisoDonarDinero);
    app.post("/contribucion/donacion-dinero", new DonacionDineroController()::create, permisoDonarDinero);
    app.get("/contribucion/entrega-tarjetas", new EntregaTarjetasController()::index, permisoSolicitarTarjetas);

    app.exception(Exception.class, (e, ctx) -> {
      ctx.status(500);
      ctx.result("Internal server error: " + e.getMessage() + "\n\n" + Arrays.toString(e.getStackTrace()));
    });
  }
}