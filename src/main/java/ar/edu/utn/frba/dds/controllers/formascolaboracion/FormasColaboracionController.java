package ar.edu.utn.frba.dds.controllers.formascolaboracion;

import ar.edu.utn.frba.dds.models.entities.users.Permiso;
import io.javalin.http.Context;

import java.util.List;
import java.util.Map;

public class FormasColaboracionController {
  public void index(Context context) {
    Map<String, Object> model = context.attribute("model");
    List<Permiso> permisos = context.sessionAttribute("permisos");
    model.put("puedeDonarDinero", tienePermiso(permisos, "Donar-Dinero"));
    model.put("puedeDonarViandas", tienePermiso(permisos, "Donar-Viandas"));
    model.put("puedeCuidarHeladera", tienePermiso(permisos, "Cuidar-Heladera"));
    model.put("puedeDistribuirVianda", tienePermiso(permisos, "Distribuir-Viandas"));
    model.put("puedeSubirRecompensa", tienePermiso(permisos, "Subir-Recompensa"));
    model.put("puedeSuscribirse", tienePermiso(permisos, "Suscribirse-Heladera"));

    context.render("formascolaboracion/formascolaboracion.hbs", model);
  }

  public Boolean tienePermiso(List<Permiso> permisos, String nombrePermiso) {
    return permisos.contains(new Permiso(nombrePermiso));
  }
}
