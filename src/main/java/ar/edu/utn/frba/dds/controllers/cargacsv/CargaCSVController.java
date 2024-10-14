package ar.edu.utn.frba.dds.controllers.cargacsv;

import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public class CargaCSVController {
  public void index(Context context) {
    Map<String, Object> model = context.attribute("model");

    context.render("cargacsv/cargacsv.hbs", model);
  }
}

