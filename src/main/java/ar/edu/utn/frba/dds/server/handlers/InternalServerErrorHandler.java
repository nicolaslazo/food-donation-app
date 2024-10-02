package ar.edu.utn.frba.dds.server.handlers;

import io.javalin.Javalin;

public class InternalServerErrorHandler implements IHandler {
  @Override
  public void setHandle(Javalin app) {
    app.exception(Exception.class, (e, ctx) -> {
      ctx.status(500);
      ctx.result("Internal Server Error");
    });
  }
}
