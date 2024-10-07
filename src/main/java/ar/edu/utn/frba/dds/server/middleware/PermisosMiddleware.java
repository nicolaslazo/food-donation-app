package ar.edu.utn.frba.dds.server.middleware;

import ar.edu.utn.frba.dds.models.entities.users.PermisoDenegadoException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class PermisosMiddleware {
  public static Handler requerirPermiso(@NotNull String permisoRequerido) {
    return ctx -> {
      Set<String> nombresPermisos = PermisosMiddleware.getNombresDePermisosDeContext(ctx);
      if (!nombresPermisos.contains(permisoRequerido)) {
        throw new PermisoDenegadoException("El usuario logueado no tiene el permiso: " + permisoRequerido);
      }
    };
  }

  private static Set<String> getNombresDePermisosDeContext(Context ctx) {
    return (Set<String>) Optional.ofNullable(ctx.sessionAttribute("permisos")).orElse(new HashSet<>());
  }
}
