package ar.edu.utn.frba.dds.controllers.contacto;

import io.javalin.http.Context;

public class ContactoController {
  public void index(Context context) {
    context.render("contacto/contacto.hbs");
  }
}
