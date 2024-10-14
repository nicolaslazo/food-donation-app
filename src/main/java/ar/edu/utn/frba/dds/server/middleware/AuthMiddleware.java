package ar.edu.utn.frba.dds.server.middleware;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class AuthMiddleware implements Handler {
  @Override
  public void handle(Context context) throws Exception {
    // Verificamos si hay un usuario autenticado en la sesión
    if (context.sessionAttribute("user_id") == null) {
      context.attribute("usuarioAutenticado", false);
      context.redirect("/colaborador/login");
    }
  }
}
