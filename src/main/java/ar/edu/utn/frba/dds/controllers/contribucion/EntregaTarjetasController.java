package ar.edu.utn.frba.dds.controllers.contribucion;

import io.javalin.http.Context;

public class EntregaTarjetasController {
  public void index(Context context) {
    context.render("contribuciones/entrega_tarjetas/entrega_tarjetas.hbs");
  }
}
