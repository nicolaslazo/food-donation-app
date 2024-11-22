package ar.edu.utn.frba.dds.controllers.heladera.incidente;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.heladera.incidente.IncidenteRepository;
import io.javalin.http.Context;

import java.util.List;
import java.util.Map;

public class VisitaTecnicaController {
  public void index(Context context) {
    Map<String, Object> model = context.attribute("model");

    List<Heladera> heladeras = new IncidenteRepository().findLasTodasHeladerasConFallaTecnica().toList();
    model.put("heladeras", heladeras);

    context.render("incidentes/carga-visita-tecnica/visita-tecnica.hbs", model);
  }

  public void create(Context context) {}
}
