package ar.edu.utn.frba.dds.server.middleware;

import ar.edu.utn.frba.dds.models.entities.users.Permiso;
import ar.edu.utn.frba.dds.models.entities.users.PermisoDenegadoException;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.security.RouteRole;

import java.util.List;
import java.util.Set;

public class PermisosMiddleware {
  public static void apply(Javalin app) {
    app.beforeMatched(ctx -> {
      List<Permiso> permisosUsuario = getPermisos(ctx);
      Set<RouteRole> permisosRequeridos = ctx.routeRoles();

      if (!permisosUsuario.containsAll(permisosRequeridos))
        throw new PermisoDenegadoException(
            "El usuario no está logueado o no tiene permisos para acceder a este recurso");
    });
  }

  private static List<Permiso> getPermisos(Context ctx) {
    return (ctx.sessionAttribute("permisos") != null) ? ctx.sessionAttribute("permisos") : List.of();
  }
}
