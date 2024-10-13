package ar.edu.utn.frba.dds.controllers.verreportes;

import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PDFGeneratorController {

  public void index(Context context) {

    // Renderizar la vista Handlebars
    context.render("verReportes/verreportes.hbs");
  }

}