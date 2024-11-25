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
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TiendaController {
  CanjeosRepository canjeosRepository = new CanjeosRepository();
  ColaboradorRepository colaboradorRepository = new ColaboradorRepository();
  RecompensasRepository recompensaRepository = new RecompensasRepository();


  public void index(Context context) {
    Map<String, Object> model = context.attribute("model");

    context.render("tienda/tienda.hbs", model);
  }

  public void indexRecompensaNueva(Context ctx) {
    Map<String, Object> model = ctx.attribute("model");

    Colaborador colaborador = colaboradorRepository.findById(ctx.sessionAttribute("user_id")).get();

    List<Recompensa> recompensas = recompensaRepository.findAll(colaborador).toList();
    model.put("recompensas", recompensas);
    model.put("categorias", RubroRecompensa.values());
    ctx.render("tienda/ofrecer.hbs", model);
  }

  public void crearRecompensa(Context context) {
    String nombre = context.formParam("nombreServicio");
    String puntos = context.formParam("puntosServicio");
    String stock = context.formParam("stockServicio");
    String categoria = context.formParam("categoriaServicio");
    Colaborador colaborador = colaboradorRepository.findById(context.sessionAttribute("user_id")).get();
    Recompensa recompensa = new Recompensa(nombre, colaborador, Long.parseLong(puntos), Integer.parseInt(stock), RubroRecompensa.valueOf(categoria), null);

    recompensaRepository.insert(recompensa);
  }

  public void indexRecompensas(Context context) {
    Map<String, Object> model = context.attribute("model");

    Colaborador colaborador = colaboradorRepository.findById(context.sessionAttribute("user_id")).get();
    model.put("puntos-disponibles", canjeosRepository.getPuntosDisponibles(colaborador));
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

  public void indexHistorial(Context context) {
    Map<String, Object> model = context.attribute("model");

    Long colaboradorId = context.sessionAttribute("user_id");

    List<Canjeo> canjeos = canjeosRepository.findCanjeosByColaboradorId(colaboradorId);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    List<Map<String, Object>> formattedCanjeos = canjeos.stream()
        .map(canjeo -> {
          Map<String, Object> map = new HashMap<>();
          map.put("nombre", canjeo.getRecompensa().getNombre());
          map.put("costoEnPuntos", canjeo.getRecompensa().getCostoEnPuntos());
          map.put("rubro", canjeo.getRecompensa().getRubro().name());
          map.put("fecha", canjeo.getFecha().format(formatter));
          return map;
        })
        .collect(Collectors.toList());

    model.put("canje", formattedCanjeos);
    model.put("categorias", Arrays.stream(RubroRecompensa.values())
        .map(Enum::name)
        .collect(Collectors.toList()));

    context.attribute("model", model);
    context.render("tienda/historial.hbs", model);
  }


}
