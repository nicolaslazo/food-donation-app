package ar.edu.utn.frba.dds.controllers.heladera.incidente;

import ar.edu.utn.frba.dds.controllers.image.ImageController;
import ar.edu.utn.frba.dds.models.entities.Tecnico;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.Incidente;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.VisitaTecnica;
import ar.edu.utn.frba.dds.models.repositories.TecnicoRepository;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.incidente.IncidenteRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.incidente.VisitasTecnicasRepository;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    List<Map<String, Object>> visitasTecnicasData;
    visitasTecnicasData = reportesVisitasTecnicas.stream().map(visitaTecnica -> {
      Map<String, Object> visitaTecnicaData = new HashMap<>();
      visitaTecnicaData.put("nombreHeladera", visitaTecnica.getIncidente().getHeladera().getNombre());
      visitaTecnicaData.put("imagen", visitaTecnica.getPathImagen()); //TODO: Porque verga no lo muestra, no lo se
      visitaTecnicaData.put("incidenteDescripcion", visitaTecnica.getIncidente().getDescripcion());
      visitaTecnicaData.put("visitaDescripcion", visitaTecnica.getDescripcion());
      visitaTecnicaData.put("fechaIncidente", formatearFecha(visitaTecnica.getIncidente().getFecha()));
      visitaTecnicaData.put("fechaVisita", formatearFechaDate(visitaTecnica.getFecha()));
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

  private String formatearFechaDate(ZonedDateTime fecha) {
    // Formato: dd/mm/aaaa - hora:minuto - 24hs
    return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
  }

  public void create(Context context) {
    try {
      String pathImagen = new ImageController().guardarImagen(context, "imagen");

      System.out.println("Ruta: " + pathImagen);

      long idHeladeraDefectuosa = Long.parseLong(context.formParam("idHeladera"));
      Heladera heladeraDefectuosa = new HeladerasRepository().findById(idHeladeraDefectuosa).get();

      Incidente incidente = new IncidenteRepository().findByHeladera(heladeraDefectuosa).get();

      String fechaInput = context.formParam("fecha");
      LocalDate localDate = LocalDate.parse(fechaInput);
      ZonedDateTime fechaParseada = localDate.atStartOfDay(ZoneId.of("America/Argentina/Buenos_Aires"));

      Boolean incidenteResuelto = false;
      if (Objects.equals(context.formParam("problemaResuelto"), "true")) {
        incidenteResuelto = true;
      }

      Tecnico tecnico = new TecnicoRepository().findById(context.sessionAttribute("user_id")).get();
      VisitaTecnica visitaTecnica = new VisitaTecnica(
          tecnico,
          incidente,
          fechaParseada,
          incidenteResuelto,
          context.formParam("descripcionReparacion"),
          pathImagen
      );

      new VisitasTecnicasRepository().insert(visitaTecnica);

      if (incidenteResuelto) {
        incidente.setFechaResuelto(fechaParseada);
        new IncidenteRepository().update(incidente);
      }

      context.json(Map.of(
              "message", "Visita Técnica registrada con éxito! Redirigiendo en tres segundos...",
              "success", true,
              "urlRedireccion", "/quiero-ayudar",
              "demoraRedireccionEnSegundos", 3
      ));
    } catch (Exception e) {
      System.out.println("Fallo: " + e.toString());
      context.json(Map.of(
              "message", "Error al cargar la visita técnica",
              "success", false
      ));
    }
  }
}
