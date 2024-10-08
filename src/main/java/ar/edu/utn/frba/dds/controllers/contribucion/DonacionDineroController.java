package ar.edu.utn.frba.dds.controllers.contribucion;

import io.javalin.http.Context;

public class DonacionDineroController {
  public void index(Context context) {
    context.render("contribuciones/donacion_dinero/donacion_dinero.hbs");
  }
}
