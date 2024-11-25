package ar.edu.utn.frba.dds.controllers.heladera.incidente;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.VisitaTecnica;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.incidente.IncidenteRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.incidente.VisitasTecnicasRepository;
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

  public void indexReporteTecnico(Context context) {
    Map<String, Object> model = context.attribute("model");

    Colaborador colaboradorResponsable = new ColaboradorRepository().findById(context.sessionAttribute("user_id")).get();

    List<VisitaTecnica> reportesVisitasTecnicas = new VisitasTecnicasRepository().findAllVisitasTecnicas(colaboradorResponsable).toList();

    model.put("visitasTecnicas", reportesVisitasTecnicas);

    context.render("incidentes/visualizacion-visita-tecnica/visita-tecnica.hbs", model);
  }

  public void create(Context context) {}
}
