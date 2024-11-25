package ar.edu.utn.frba.dds.controllers.heladera.incidente;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.VisitaTecnica;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.incidente.IncidenteRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.incidente.VisitasTecnicasRepository;
import io.javalin.http.Context;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
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

    List<Map<String, Object>> visitasTecnicasData;

    List<VisitaTecnica> reportesVisitasTecnicas = new VisitasTecnicasRepository().findAllVisitasTecnicas(colaboradorResponsable).toList();

    visitasTecnicasData = reportesVisitasTecnicas.stream().map(visitaTecnica -> {
      Map<String, Object> visitaTecnicaData = new HashMap<>();
      visitaTecnicaData.put("nombreHeladera", visitaTecnica.getIncidente().getHeladera().getNombre());
      visitaTecnicaData.put("imagen", visitaTecnica.getImagen());
      visitaTecnicaData.put("incidenteDescripcion", visitaTecnica.getIncidente().getDescripcion());
      visitaTecnicaData.put("visitaDescripcion", visitaTecnica.getDescripcion());
      visitaTecnicaData.put("fechaIncidente", formatearFecha(visitaTecnica.getIncidente().getFecha()));
      visitaTecnicaData.put("fechaVisita", formatearFecha(visitaTecnica.getFecha()));
      visitaTecnicaData.put("incidenteResuelto", visitaTecnica.getIncidenteResuelto());
      return visitaTecnicaData;
    }).toList();

    model.put("visitasTecnicas", visitasTecnicasData);

    context.render("incidentes/visualizacion-visita-tecnica/visita-tecnica.hbs", model);
  }

  private String formatearFecha(ZonedDateTime fecha) {
    // Formato: dd/mm/aaaa - hora:minuto - 24hs
    return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm"));
  }

  public void create(Context context) {}
}
