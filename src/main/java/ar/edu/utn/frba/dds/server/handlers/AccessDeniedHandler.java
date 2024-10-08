package ar.edu.utn.frba.dds.server.handlers;

import ar.edu.utn.frba.dds.models.entities.users.PermisoDenegadoException;
import io.javalin.Javalin;

public class AccessDeniedHandler implements IHandler {

  @Override
  public void setHandle(Javalin app) {
    app.exception(PermisoDenegadoException.class, (e, context) -> {
      context.status(401);
      context.render("401.hbs");
    });
  }
}
