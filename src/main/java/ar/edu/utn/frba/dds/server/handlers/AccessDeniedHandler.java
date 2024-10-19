package ar.edu.utn.frba.dds.server.handlers;

import ar.edu.utn.frba.dds.models.entities.users.PermisoDenegadoException;
import io.javalin.Javalin;

import java.util.Map;

public class AccessDeniedHandler implements IHandler {

  @Override
  public void setHandle(Javalin app) {
    app.exception(PermisoDenegadoException.class, (e, context) -> {
      Map<String, Object> model = context.attribute("model");
      context.status(401);
      context.render("401.hbs", model);
    });
  }
}
