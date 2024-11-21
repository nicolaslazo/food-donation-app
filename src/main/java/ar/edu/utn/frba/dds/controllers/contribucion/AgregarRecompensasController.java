package ar.edu.utn.frba.dds.controllers.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.OfertaRecompensa;
import ar.edu.utn.frba.dds.models.entities.contribucion.RubroRecompensa;
import ar.edu.utn.frba.dds.models.entities.recompensas.Recompensa;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.contribucion.OfertaRecompensaRepository;
import ar.edu.utn.frba.dds.models.repositories.recompensas.RecompensasRepository;
import io.javalin.http.Context;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

public class AgregarRecompensasController {
  public void index(Context context) {
    Map<String, Object> model = context.attribute("model");
    context.render("contribuciones/agregar_recompensa/agregar_recompensa.hbs", model);
  }

  public void create(Context context) {
    Colaborador colaborador = new ColaboradorRepository().findById(context.sessionAttribute("user_id")).get();
    String nombre = context.formParam("nombreRecompensa");
    Long costoEnPuntos = Long.parseLong(Objects.requireNonNull(context.formParam("costoPuntos")));
    Integer stockInicial = Integer.parseInt(Objects.requireNonNull(context.formParam("stockInicial")));
    RubroRecompensa rubro = RubroRecompensa.valueOf(Objects.requireNonNull(context.formParam("rubro")).toUpperCase());
    URL imagen = null;

    try {
      String imagenUrl = context.formParam("imagen");
      if (imagenUrl != null && !imagenUrl.isEmpty()) {
        imagen = new URL(imagenUrl);
      }
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException("La URL de la imagen no es válida", e);
    }

    Recompensa recompensa = new Recompensa(
        nombre, colaborador, costoEnPuntos, stockInicial, rubro, imagen);
    new RecompensasRepository().insert(recompensa);
    OfertaRecompensa ofertaRecompensa = new OfertaRecompensa(colaborador,
        recompensa);
    new OfertaRecompensaRepository().insert(ofertaRecompensa);

    context.redirect("/quiero-ayudar");
  }
}