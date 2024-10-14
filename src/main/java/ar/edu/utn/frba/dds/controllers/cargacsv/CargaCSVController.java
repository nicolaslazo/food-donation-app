package ar.edu.utn.frba.dds.controllers.cargacsv;

import io.javalin.http.Context;

public class CargaCSVController {
  public void index(Context context) {
    context.render("cargacsv/cargacsv.hbs");
  }
}

