package ar.edu.utn.frba.dds.controllers.tienda;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.RubroRecompensa;
import ar.edu.utn.frba.dds.models.entities.recompensas.Canjeo;
import ar.edu.utn.frba.dds.models.entities.recompensas.Recompensa;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.recompensas.CanjeosRepository;
import ar.edu.utn.frba.dds.models.repositories.recompensas.RecompensasRepository;
import io.javalin.http.Context;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TiendaController {
  CanjeosRepository canjeosRepository = new CanjeosRepository();
  ColaboradorRepository colaboradorRepository = new ColaboradorRepository();
  RecompensasRepository recompensaRepository = new RecompensasRepository();

  public void index(Context context) {
    context.render("tienda/tienda.hbs");
  }

  public void indexRecompensaNueva(Context ctx) {
    Colaborador colaborador = colaboradorRepository.findById(ctx.sessionAttribute("user_id")).get();

    Map<String, Object> model = new HashMap<>();
    List<Recompensa> recompensas = recompensaRepository.findAll(colaborador).toList();
    model.put("recompensas", recompensas);
    model.put("categorias", RubroRecompensa.values());
    ctx.render("tienda/ofrecer.hbs", model);
  }

  public void crearRecompensa(Context ctx) {
    String nombre = ctx.formParam("nombreServicio");
    String puntos = ctx.formParam("puntosServicio");
    String stock = ctx.formParam("stockServicio");
    String categoria = ctx.formParam("categoriaServicio");
    Colaborador colaborador = colaboradorRepository.findById(ctx.sessionAttribute("user_id")).get();
    Recompensa recompensa = new Recompensa(nombre, colaborador, Long.parseLong(puntos), Integer.parseInt(stock), RubroRecompensa.valueOf(categoria), null);

    recompensaRepository.insert(recompensa);
  }

  public void indexRecompensas(Context context) {
    Colaborador colaborador = colaboradorRepository.findById(context.sessionAttribute("user_id")).get();
    Map<String, Object> model = new HashMap<>();
    model.put("my-puntos", canjeosRepository.getPuntosDisponibles(colaborador));
    model.put("categorias", RubroRecompensa.values());
    model.put("items", recompensaRepository.findAllConStock().toList());
    context.render("tienda/canjear.hbs", model);
  }

  public void canjearRecompensa(Context ctx) {
    Colaborador colaborador = colaboradorRepository.findById(ctx.sessionAttribute("user_id")).get();
    Recompensa recompensa = recompensaRepository.findById(Long.parseLong(ctx.pathParam("id"))).get();

    if (recompensaRepository.findStock(recompensa) <= 0)
      throw new RuntimeException("Esta recompensa ya no tiene stock");

    canjeosRepository.insert(new Canjeo(colaborador, recompensa, ZonedDateTime.now()));
  }
}
