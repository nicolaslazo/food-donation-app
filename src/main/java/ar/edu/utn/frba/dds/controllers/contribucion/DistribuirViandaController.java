package ar.edu.utn.frba.dds.controllers.contribucion;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import io.javalin.http.Context;

import java.util.List;
import java.util.Map;

public class DistribuirViandaController {
  public void index(Context context) {
    Map<String, Object> model = context.attribute("model");
    List<Heladera> heladeras = new HeladerasRepository().findAll().toList();
    model.put("heladeras", heladeras);
    context.render("contribuciones/distribuir_viandas/distribuir_vianda.hbs", model);
  }
}
