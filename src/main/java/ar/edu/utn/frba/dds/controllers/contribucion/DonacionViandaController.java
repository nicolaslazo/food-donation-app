package ar.edu.utn.frba.dds.controllers.contribucion;

import ar.edu.utn.frba.dds.dtos.input.contribucion.DonacionViandaInputDTO;
import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.DonacionViandas;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.ViandasRepository;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.contribucion.DonacionViandasRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DonacionViandaController {
  public void registrarDonacionViandas(List<DonacionViandaInputDTO> dtos) {
    // Recupero al colaborador
    long idColaborador = dtos.stream().findFirst().get().getIdColaborador();
    Colaborador colaborador = new ColaboradorRepository().findById(idColaborador).get();

    // Recupero a la Heladera donde se depositaran las Viandas
    long idHeladera = dtos.stream().findFirst().get().getIdHeladera();
    Heladera heladeraDestino = new HeladerasRepository().findById(idHeladera).get();

    /* TODO: Nota: No se settea la Vianda a la Heladera hasta que no ingrese físicamente */
    // Recorro los dtos para Instanciar las Viandas
    Collection<Vianda> viandas = new ArrayList<>();
    for (DonacionViandaInputDTO dto : dtos) {
      Vianda viandaNueva = dto.getVianda();

      viandas.add(viandaNueva);
      // Guardo las Viandas, pero sin Heladera setteada
      new ViandasRepository().insert(viandaNueva);
    }
    DonacionViandas donacionViandas = new DonacionViandas(
        colaborador,
        viandas,
        heladeraDestino
    );
    new DonacionViandasRepository().insert(donacionViandas);
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
      return heladeraData;
    }).collect(Collectors.toList());

    model.put("heladeras", heladerasData);

    context.render("contribuciones/donacion_vianda/donacion_vianda.hbs", model);
  }

  public void create(Context context) {
    // Recupero el ID de la Heladera donde se depositaran las viandas
    long idHeladera = Long.parseLong(context.formParam("idHeladera"));

    // Recupero y Parseo la fecha en que se completo el Formulario
    ZonedDateTime fechaDonacionParseada = ZonedDateTime.parse(context.formParam("fechaHora"));

    // Creo viandas dependiendo de la cantidad de viandas que se ingresan en el formulario
    List<DonacionViandaInputDTO> dtos = new ArrayList<>();
    for (int i = 1; i <= Integer.parseInt(context.formParam("cantidad")); i++) {
      // Parseo la Fecha que se trabaja con LocalDate desde el formulario
      LocalDate fechaCaducidad = LocalDate.parse(context.formParam(String.format("fechaCaducidad_%s", i)));
      ZonedDateTime fechaCaducidadParseada = fechaCaducidad.atStartOfDay(ZoneId.systemDefault());

      // Comienzo con la creación del DTO
      DonacionViandaInputDTO dto = new DonacionViandaInputDTO(
          context.sessionAttribute("user_id"),
          context.formParam(String.format("descripcionVianda_%s", i)),
          fechaCaducidadParseada,
          fechaDonacionParseada,
          Double.parseDouble(context.formParam(String.format("pesoVianda_%s", i))),
          Integer.parseInt(context.formParam(String.format("calorias_%s", i))),
          idHeladera
      );
      dtos.add(dto);
    }
    registrarDonacionViandas(dtos);
    context.redirect("/formas-colaboracion");
  }
}
