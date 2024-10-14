package ar.edu.utn.frba.dds.controllers.tienda;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.RubroRecompensa;
import ar.edu.utn.frba.dds.models.entities.recompensas.Canjeo;
import ar.edu.utn.frba.dds.models.entities.recompensas.Recompensa;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.recompensas.CanjeosRepository;
import ar.edu.utn.frba.dds.models.repositories.recompensas.RecompensasRepository;
import io.javalin.http.Context;

public class TiendaController {
  CanjeosRepository canjeosRepository = new CanjeosRepository();
  ColaboradorRepository colaboradorRepository = new ColaboradorRepository();
  RecompensasRepository recompensaRepository = new RecompensasRepository();

  public void index(Context context) {
    context.render("tienda/home/tienda.hbs");
  }
  
  public static String[] getEnumNames(Class<? extends Enum<?>> enumClass) {
    return Arrays.stream(enumClass.getEnumConstants())
                 .map(Enum::name)
                 .toArray(String[]::new);
  }

  public void ofrecerProducto(Context ctx) {
    Colaborador colaborador = colaboradorRepository.findById(ctx.sessionAttribute("user_id")).get();
    // Colaborador colaborador = colaboradorRepository.findById(3L).get();
    Map<String, Object> model = new HashMap<>();
    List<Recompensa> recompensas = recompensaRepository.getAllMyRecompensas(colaborador);
    model.put("recompensas", recompensas);
    model.put("categorias", getEnumNames(RubroRecompensa.class));
    ctx.render("tienda/ofrecerRecompensaEmpresa/ofrecerProductoServicio.hbs", model);
  }

  public void createProducto(Context ctx) {
    String nombre = ctx.formParam("nombreServicio");
    String puntos = ctx.formParam("puntosServicio");
    String stock = ctx.formParam("stockServicio");
    String categoria = ctx.formParam("categoriaServicio");
    Colaborador colaborador = colaboradorRepository.findById(ctx.sessionAttribute("user_id")).get();
    // Colaborador colaborador = colaboradorRepository.findById(3L).get();
    Recompensa recompensa = new Recompensa(nombre, colaborador, Double.parseDouble(puntos), Integer.parseInt(stock), RubroRecompensa.valueOf(categoria), null);

    recompensaRepository.insert(recompensa);
    ctx.redirect("/tienda/ofrecerProducto");
  }

  public void deleteProducto(Context ctx) {
    recompensaRepository.deleteByID(Long.parseLong(ctx.pathParam("id")));
  }

  public void canjearProductos(Context context) {
    Colaborador colaborador = colaboradorRepository.findById(context.sessionAttribute("user_id")).get();
    // Colaborador colaborador = colaboradorRepository.findById(3L).get();
    Map<String, Object> model = new HashMap<>();
    model.put("my-puntos", canjeosRepository.getPuntosDisponibles(colaborador));
    model.put("categorias", getEnumNames(RubroRecompensa.class));
    model.put("items", recompensaRepository.findAll().toList());
    context.render("tienda/canjearPuntos/canjePuntosProductosServicios.hbs", model);
  }

  public void canjearProductosPost(Context ctx) {
    Colaborador colaborador = colaboradorRepository.findById(ctx.sessionAttribute("user_id")).get();
    // Colaborador colaborador = colaboradorRepository.findById(3L).get();
    Recompensa recompensa = recompensaRepository.findById(Long.parseLong(ctx.pathParam("id"))).get();
    canjeosRepository.insert(new Canjeo(colaborador, recompensa, ZonedDateTime.now()));
  }

  public void modifyProducto(Context ctx) {
    Recompensa recompensa = recompensaRepository.findById(Long.parseLong(ctx.pathParam("id"))).get();
    recompensa.setCostoEnPuntos(Double.parseDouble(ctx.formParam("puntos")));
    recompensa.setStockInicial(Integer.parseInt(ctx.formParam("stock")));
    recompensaRepository.update(recompensa);
    ctx.redirect("/tienda/ofrecerProducto");
  }

}
