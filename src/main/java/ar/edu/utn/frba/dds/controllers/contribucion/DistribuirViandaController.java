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

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DistribuirViandaController {
  public void generarPDF(RedistribucionViandas redistribucionViandas) {
    // TODO: Generar un PDF con los datos de las viandasa y de la distribución en general.
  }

  public void index(Context context) {
    Map<String, Object> model = context.attribute("model");

    // Recupero heladeras
    HeladerasRepository heladerasRepository = new HeladerasRepository();

    List<Map<String, Object>> heladerasData;
    List<Map<String, Object>> viandasData;

    List<Heladera> heladeras = heladerasRepository.findAllToList();
    // Tomo información de las heladeras.
    heladerasData = heladeras.stream().map(heladera -> {
      Map<String, Object> heladeraData = new HashMap<>();
      heladeraData.put("idHeladera", heladera.getId());
      heladeraData.put("lat", heladera.getUbicacion().getLatitud());
      heladeraData.put("long", heladera.getUbicacion().getLongitud());
      heladeraData.put("nombre", heladera.getNombre());
      heladeraData.put("capacidadDisponible", heladerasRepository.getCapacidadDisponible(heladera));
      heladeraData.put("viandasDepositadas", heladerasRepository.getCantidadViandasDepositadas(heladera));
      return heladeraData;
    }).toList();

    model.put("heladeras", heladerasData);

    // Recupero las Viandas
    ViandasRepository viandasRepository = new ViandasRepository();

    List<Vianda> viandas = viandasRepository.findAllToList();
    // Tomo información de las Viandas, excluyendo las que no tienen heladera física asociada
    viandasData = viandas.stream()
            .filter(vianda -> vianda.getHeladera() != null) // Solo cargamos viandas que estan depositadas en una heladera
            .map(vianda -> {
              Map<String, Object> viandaData = new HashMap<>();
              viandaData.put("idVianda", vianda.getId());
              viandaData.put("idHeladera", vianda.getHeladera().getId());
              viandaData.put("descripcion", vianda.getDescripcion());
              viandaData.put("fechaCaducidad", DateTimeFormatter.ofPattern("yyyy-MM-dd").format(vianda.getFechaCaducidad()));
              viandaData.put("pesoVianda", vianda.getPesoEnGramos());
              return viandaData;
            })
            .toList();

    model.put("viandas", viandasData);
    context.render("contribuciones/distribuir_viandas/distribuir_vianda.hbs", model);
  }

  public void create(Context context) {
    try {
      // Recupero los IDs de las Heladeras donde se retiran y depositaran las viandas
      long idHeladeraOrigen = Long.parseLong(context.formParam("idHeladeraOrigen"));
      long idHeladeraDestino = Long.parseLong(context.formParam("idHeladeraDestino"));

      // Recupero los IDs de las Viandas como un String y luego divido en una lista de Strings
      String viandasIDsString = context.formParam("viandasIds"); // Recibe el String "2,3,5"
      List<Long> viandasIDs = Arrays.stream(viandasIDsString.split(",")) // Separa el String por comas
              .map(Long::parseLong) // Convierte cada elemento a Long
              .toList();

      DistribuirViandaInputDTO dto = new DistribuirViandaInputDTO(
              context.sessionAttribute("user_id"),
              idHeladeraOrigen,
              idHeladeraDestino,
              viandasIDs,
              context.formParam("motivo").toUpperCase()
      );
      create(dto);

      // TODO: Hacer que se le descargue el PDF al Colaborador
      context.json(Map.of(
              "message", "Colaboración registrada con éxito! Redirigiendo en tres segundos...",
              "success", true,
              "urlRedireccion", "/formas-colaboracion",
              "demoraRedireccionEnSegundos", 3
      ));
    } catch (Exception e) {
      context.json(Map.of(
              "message", "Error al registirar la colaboración.",
              "success", false
      ));
    }
  }

  // Registro la solicitud de Distribución de viandas
  public void create(DistribuirViandaInputDTO dto) {
    ViandasRepository repositorioViandas = new ViandasRepository();
    HeladerasRepository repositorioHeladeras = new HeladerasRepository();

    // Recupero de la BD al Colaborador
    Colaborador colaborador = new ColaboradorRepository().findById(dto.getIdColaborador()).get();

    // Recupero de la BD las Heladeras origen y Destino
    Heladera heladeraOrigen = repositorioHeladeras.findById(dto.getIdHeladeraOrigen()).get();
    Heladera heladeraDestino = repositorioHeladeras.findById(dto.getIdHeladeraDestino()).get();

    Collection<Vianda> viandasADistribuir = new ArrayList<>();
    // Recorro y obtengo las viandas a distribuir
    for (Long idVianda : dto.getIdsViandas()) {
      // Obtengo la vianda y la retiro de la heladera
      Vianda viandaADistribuir = repositorioViandas.findById(idVianda).get();
      viandaADistribuir.setHeladera(null);

      // Actualizo el estado de la vianda
      repositorioViandas.persist(viandaADistribuir);

      viandasADistribuir.add(viandaADistribuir);
    }
    repositorioHeladeras.persist(heladeraOrigen);

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
}
