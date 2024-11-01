package ar.edu.utn.frba.dds.controllers.mapa;

import io.javalin.http.Context;
import java.util.Map;

public class MapaController {
  public void index(Context context) {
    Map<String, Object> model = context.attribute("model");

    context.render("mapa/mapa.hbs");
  }
}
