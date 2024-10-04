package ar.edu.utn.frba.dds.controllers.verreportes;

import io.javalin.http.Context;

public class PDFGeneratorController {

  public void index(Context context) {
    context.render("verReportes/verreportes.hbs");
  }

}