package ar.edu.utn.frba.dds.controllers;

import io.javalin.http.Context;

public class PersonaVulnerableController {
  public void index(Context context) {
    context.render("cargapersonavulnerable/personavulnerable.hbs");
  }
}
