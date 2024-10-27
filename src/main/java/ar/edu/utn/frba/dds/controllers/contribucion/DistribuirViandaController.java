package ar.edu.utn.frba.dds.controllers.contribucion;

import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DistribuirViandaController {
  public void index(Context context) {
    Map<String, Object> model = context.attribute("model");

    // Recupero heladeras
    HeladerasRepository heladerasRepository = new HeladerasRepository();
    List<Heladera> heladeras = heladerasRepository.findAll().toList();

    // Tomo información de las heladeras.
    List<Map<String, Object>> heladerasData = heladeras.stream().map(heladera -> {
      Map<String, Object> heladeraData = new HashMap<>();
      heladeraData.put("idHeladera", heladera.getId());
      heladeraData.put("lat", heladera.getUbicacion().getLatitud());
      heladeraData.put("long", heladera.getUbicacion().getLongitud());
      heladeraData.put("nombre", heladera.getNombre());
      heladeraData.put("capacidadDisponible", heladerasRepository.getCapacidadDisponible(heladera));
      heladeraData.put("viandasDepositadas", heladerasRepository.getCantidadViandasDepositadas(heladera));
      return heladeraData;
    }).collect(Collectors.toList());

    model.put("heladeras", heladerasData);

    context.render("contribuciones/distribuir_viandas/distribuir_vianda.hbs", model);
  }

  public void create(Context context) {

  }
}
