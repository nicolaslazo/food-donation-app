package ar.edu.utn.frba.dds.controllers.session;

import io.javalin.http.Context;

public class SessionController {
  public void index(Context context) {
    context.render("/logueo/login/login.hbs");
  }
}
