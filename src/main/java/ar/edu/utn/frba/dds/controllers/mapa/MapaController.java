package ar.edu.utn.frba.dds.controllers.mapa;

import ar.edu.utn.frba.dds.models.entities.PersonaVulnerable;
import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.PersonaVulnerableRepository;
import ar.edu.utn.frba.dds.models.repositories.ViandasRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import io.javalin.http.Context;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapaController {
  public void index(Context context) {
    //TODO: Estería bueno poder mover toda esta lógica a otro lugar.
    Map<String, Object> model = context.attribute("model");

    // --- Recupero heladeras ---
    HeladerasRepository heladerasRepository = new HeladerasRepository();

    List<Map<String, Object>> heladerasData;
    List<Map<String, Object>> viandasData;

    List<Heladera> heladeras = heladerasRepository.findHeladerasConViandas().toList();
    // Tomo información de las heladeras.
    heladerasData = heladeras.stream().map(heladera -> {
      Map<String, Object> heladeraData = new HashMap<>();
      heladeraData.put("idHeladera", heladera.getId());
      heladeraData.put("lat", heladera.getUbicacion().getLatitud());
      heladeraData.put("long", heladera.getUbicacion().getLongitud());
      heladeraData.put("nombre", heladera.getNombre());
      heladeraData.put("viandasDepositadas", heladerasRepository.getCantidadViandasDepositadas(heladera));
      return heladeraData;
    }).toList();

    model.put("heladeras", heladerasData);

    // --- Recupero las Viandas ---
    ViandasRepository viandasRepository = new ViandasRepository();

    List<Vianda> viandas = viandasRepository.findAll().toList();
    // Tomo información de las Viandas, excluyendo las que no tienen heladera física asociada
    viandasData = viandas.stream()
            // TODO: Hacer este filtrado en la query a la db para menos transferencia de datos
            .filter(vianda -> vianda.getHeladera() != null) // Solo cargamos viandas que estan depositadas en una heladera
            .map(vianda -> {
              Map<String, Object> viandaData = new HashMap<>();
              viandaData.put("idVianda", vianda.getId());
              viandaData.put("idHeladera", vianda.getHeladera().getId());
              viandaData.put("descripcion", vianda.getDescripcion());
              viandaData.put("fechaCaducidad", DateTimeFormatter.ofPattern("yyyy-MM-dd").format(vianda.getFechaCaducidad()));
              viandaData.put("pesoVianda", vianda.getPesoEnGramos());
              viandaData.put("caloriasVianda", vianda.getCaloriasVianda());
              return viandaData;
            })
            .toList();

    model.put("viandas", viandasData);

    // --- Recupero los datos de la Persona Vulnerable ---
    // Unicamente si esta Logueada
    if (context.sessionAttribute("esPersonaVulnerable")) {
      PersonaVulnerable personaVulnerable = new PersonaVulnerableRepository().findById(context.sessionAttribute("user_id")).get();
      model.put("nombre", personaVulnerable.getUsuario().getPrimerNombre());
      model.put("apellido", personaVulnerable.getUsuario().getApellido());
      model.put("menoresACargo", personaVulnerable.getMenoresACargo());
      model.put("usosDisponibles", 5); //TODO: El controller de las tarjetas debería de brindarnos esta lógica.
      model.put("userRol", "PERSONAVULNERABLE");
    } else {
      model.put("userRol", "OTHER");
    }

    context.render("mapa/mapa.hbs", model);
  }
}
