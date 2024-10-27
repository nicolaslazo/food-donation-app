package ar.edu.utn.frba.dds.controllers.contribucion;

import ar.edu.utn.frba.dds.dtos.input.contribucion.DistribuirViandaInputDTO;
import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.MotivoDeDistribucion;
import ar.edu.utn.frba.dds.models.entities.contribucion.RedistribucionViandas;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.ViandasRepository;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.contribucion.RedistribucionViandasRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DistribuirViandaController {
  // Registro la solicitud de Distribución de viandas
  public void registrarDistribucionVianda(DistribuirViandaInputDTO dto) {
    ViandasRepository repositorioViandas = new ViandasRepository();
    HeladerasRepository repositorioHeladeras = new HeladerasRepository();

    // Recupero de la BD al Colaborador
    Colaborador colaborador = new ColaboradorRepository().findById(dto.getIdColaborador()).get();

    // Recupero de la BD las Heladeras origen y Destino
    Heladera heladeraOrigen = repositorioHeladeras.findById(dto.getIdHeladeraOrigen()).get();
    Heladera heladeraDestino = repositorioHeladeras.findById(dto.getIdHeladeraDestino()).get();

    List<Vianda> viandas = repositorioViandas.findAll(heladeraOrigen).toList();

    Collection<Vianda> viandasADistribuir = new ArrayList<>();
    // Recorro y obtengo las viandas a distribuir
    for (int i = 0; i < dto.getCantidadViandas(); i++) {
      // Obtengo la vianda y la retiro de la heladera
      Vianda viandaADistribuir = viandas.get(i);
      viandaADistribuir.setHeladera(null);

      // Actualizo el estado de la vianda
      repositorioViandas.merge(viandaADistribuir);

      viandasADistribuir.add(viandaADistribuir);
    }

    // Instancio la redistribución y la registro
    RedistribucionViandas redistribucionViandas = new RedistribucionViandas(
            colaborador,
            viandasADistribuir,
            heladeraDestino,
            heladeraOrigen,
            MotivoDeDistribucion.valueOf(dto.getMotivoDistribucion())
    );
    new RedistribucionViandasRepository().insert(redistribucionViandas);

    //generarPDF(redistribucionViandas);
  }

  public void generarPDF(RedistribucionViandas redistribucionViandas) {
    // TODO: Generar un PDF con los datos de las viandasa y de la distribución en general.
  }

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
    try {
      // Recupero los IDs de las Heladeras donde se retiran y depositaran las viandas
      long idHeladeraOrigen = Long.parseLong(context.formParam("idHeladeraOrigen"));
      long idHeladeraDestino = Long.parseLong(context.formParam("idHeladeraDestino"));

      DistribuirViandaInputDTO dto = new DistribuirViandaInputDTO(
              context.sessionAttribute("user_id"),
              idHeladeraOrigen,
              idHeladeraDestino,
              Integer.parseInt(context.formParam("cantidadViandas")),
              context.formParam("motivo").toUpperCase()
      );
      registrarDistribucionVianda(dto);

      // TODO: Hacer que se le descargue el PDF al Colaborador
      context.json(Map.of(
              "message", "Formulario completado con exito! Redirigiendo en tres segundos...",
              "success", true,
              "urlRedireccion", "/formas-colaboracion",
              "demoraRedireccionEnSegundos", 3
      ));
    } catch (Exception e) {
      context.json(Map.of(
              "message", "¡Error al completar el formulario!",
              "success", false
      ));
    }
  }
}
