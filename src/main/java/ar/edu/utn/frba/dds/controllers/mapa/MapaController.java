package ar.edu.utn.frba.dds.controllers.mapa;

import ar.edu.utn.frba.dds.models.entities.PersonaVulnerable;
import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.PersonaVulnerableRepository;
import ar.edu.utn.frba.dds.models.repositories.ViandasRepository;
import ar.edu.utn.frba.dds.models.repositories.documentacion.TarjetasRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.SolicitudAperturaPorConsumicionRepository;
import io.javalin.http.Context;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapaController {
  public void index(Context context) {
    // TODO: Mover lógica para la recuperación de heladeras.
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

    Boolean puedeAbrirHeladeraPorConsumicion = context.sessionAttribute("abrirHeladeraConsumicion");
    if (puedeAbrirHeladeraPorConsumicion == null) {
      puedeAbrirHeladeraPorConsumicion = false;
    }

    // --- Recupero los datos de la Persona Vulnerable ---
    // Unicamente si esta Logueada
    if (context.sessionAttribute("esPersonaVulnerable")) { // Si posee el Rol de Persona Vulnerable, completo sus datos
      PersonaVulnerable personaVulnerable = new PersonaVulnerableRepository().findById(context.sessionAttribute("user_id")).get();

      Tarjeta tarjeta = new TarjetasRepository().findByRecipiente(personaVulnerable.getUsuario()).get();
      long cantidadUsosTarjetaHoy = new SolicitudAperturaPorConsumicionRepository().findCantidadUsadasHoy(tarjeta);
      long cantidadUsosDisponiblesHoy = personaVulnerable.getCantidadViandasPermitidasPorDia() - cantidadUsosTarjetaHoy;

      model.put("nombre", personaVulnerable.getUsuario().getPrimerNombre());
      model.put("apellido", personaVulnerable.getUsuario().getApellido());
      model.put("menoresACargo", personaVulnerable.getMenoresACargo());
      model.put("usosDisponibles", cantidadUsosDisponiblesHoy);
      model.put("userRol", "PERSONAVULNERABLE");
    } else if (puedeAbrirHeladeraPorConsumicion) { // Si posee el Permiso para Abrir Por consumición, se completan estos datos
      // Este caso contempla que los Admins pueden acceder a esta pestaña. Realmente no debería de estar esto, pero bueno.
      model.put("usosDisponibles", 20); // Esto esta Hardcodeado pq en sí no abren la heladera para retirar por consumisión
      model.put("userRol", "PERSONAVULNERABLE");
    } else { // No posee permisos para abrir por consumición
      model.put("userRol", "OTHER");
    }

    context.render("mapa/mapa.hbs", model);
  }
}
