package ar.edu.utn.frba.dds.controllers.contribucion;

import io.javalin.http.Context;

public class AgregarRecompensasController {
  public void index(Context context) {
    context.render("contribuciones/agregar_recompensa/agregar_recompensa.hbs");
  }
}