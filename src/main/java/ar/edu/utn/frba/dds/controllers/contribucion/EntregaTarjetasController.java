package ar.edu.utn.frba.dds.controllers.contribucion;

import io.javalin.http.Context;

import java.util.Map;

public class EntregaTarjetasController {
  public void index(Context context) {
    Map<String, Object> model = context.attribute("model");

    context.render("contribuciones/entrega_tarjetas/entrega_tarjetas.hbs", model);
  }
}
