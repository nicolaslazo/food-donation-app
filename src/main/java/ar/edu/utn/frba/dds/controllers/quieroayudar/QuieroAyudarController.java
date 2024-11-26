package ar.edu.utn.frba.dds.controllers.quieroayudar;

import ar.edu.utn.frba.dds.models.entities.users.Permiso;
import io.javalin.http.Context;

import java.util.Map;
import java.util.List;

public class QuieroAyudarController {
  public void index(Context context) {
    Map<String, Object> model = context.attribute("model");
    List<Permiso> permisos = context.sessionAttribute("permisos");
    model.put("puedeReportar", tienePermiso(permisos, "Crear-Reportes"));
    model.put("puedeContribuir", tienePermiso(permisos, "Realizar-Contribucion"));
    model.put("puedeRegistrarPersonaVulnerable", tienePermiso(permisos, "Registrar-Persona-Vulnerable"));
    model.put("puedeVerReportes", tienePermiso(permisos, "Ver-Reportes"));
    model.put("puedeVerAlertas", tienePermiso(permisos, "Ver-Alertas"));
    model.put("puedeCargarCSV", tienePermiso(permisos, "Cargar-CSV"));
    model.put("puedeCargarTecnico", tienePermiso(permisos, "Crear-Tecnico"));
    model.put("puedeCargarVisitaTecnica", tienePermiso(permisos, "Cargar-Visita-Tecnica"));

    context.render("quieroayudar/quieroayudar.hbs", model);
  }

  public Boolean tienePermiso(List<Permiso> permisos, String nombrePermiso) {
    return permisos.contains(new Permiso(nombrePermiso));
  }
}
