package ar.edu.utn.frba.dds.controllers.verreportes;

import ar.edu.utn.frba.dds.dtos.output.heladera.ReportesDTO;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PDFGeneratorController {

  public void index(Context context) {
    List<ReportesDTO> reportes = new ArrayList<>();
    reportes.add(new ReportesDTO("Reporte cantidad de Fallas por Heladera", "/public/static/ReporteFallasHeladera.pdf"));
    reportes.add(new ReportesDTO("Reporte cantidad de Viandas Retiradas/Colocadas por Heladera", "/public/static/ReporteMovimientosHeladera.pdf"));
    reportes.add(new ReportesDTO("Cantidad de Viandas por colaborador", "/public/static/ReporteViandasPorColaborador.pdf"));

    Map<String, Object> model = new HashMap<>();
    model.put("reportes", reportes);

    // Renderizar la vista Handlebars
    context.render("verReportes/verreportes.hbs",model);
  }

}