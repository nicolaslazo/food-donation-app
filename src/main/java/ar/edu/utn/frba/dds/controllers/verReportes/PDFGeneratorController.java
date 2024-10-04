package ar.edu.utn.frba.dds.controllers.verReportes;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.contribucion.DonacionViandasRepository;

import ar.edu.utn.frba.dds.models.repositories.heladera.incidente.IncidenteRepository;
import ar.edu.utn.frba.dds.services.generadorpdf.PdfGenerator;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public class PDFGeneratorController {

  public void index(Context context) {
    context.render("verReportes/verReportes.hbs");
  }

}