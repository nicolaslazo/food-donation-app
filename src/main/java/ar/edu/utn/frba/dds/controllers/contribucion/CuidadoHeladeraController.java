package ar.edu.utn.frba.dds.controllers.contribucion;

import ar.edu.utn.frba.dds.dtos.input.contribucion.CuidadoHeladeraInputDTO;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Suscripcion;
import ar.edu.utn.frba.dds.models.entities.contribucion.MotivoDeDistribucion;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.contacto.SuscripcionRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import io.javalin.http.Context;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CuidadoHeladeraController {
  public static Heladera tomarCuidadoHeladera(String dtoCuidadoJson) {
    CuidadoHeladeraInputDTO dtoCuidado = CuidadoHeladeraInputDTO.desdeJson(dtoCuidadoJson);

    Colaborador encargado = new ColaboradorRepository()
        .findById(dtoCuidado.getIdColaborador())
        .orElseThrow();

    Heladera heladeraNueva = new Heladera(dtoCuidado.getNombreHeladera(),
        dtoCuidado.getUbicacion(),
        encargado,
        dtoCuidado.getCapacidadEnViandas(),
        ZonedDateTime.now(),
        dtoCuidado.getBarrio());

    new HeladerasRepository().insert(heladeraNueva);
    new SuscripcionRepository()
        .insert(new Suscripcion(encargado, heladeraNueva, MotivoDeDistribucion.FALLA_HELADERA, null));

    return heladeraNueva;
  }

  public static Heladera tomarCuidadoHeladera(CuidadoHeladeraInputDTO dtoCuidado) {
    Colaborador encargado = new ColaboradorRepository().findById(dtoCuidado.getIdColaborador()).get();

    Heladera heladeraNueva = new Heladera(dtoCuidado.getNombreHeladera(),
        dtoCuidado.getUbicacion(),
        encargado,
        dtoCuidado.getCapacidadEnViandas(),
        ZonedDateTime.now(),
        dtoCuidado.getBarrio());

    new HeladerasRepository().insert(heladeraNueva);
    new SuscripcionRepository()
        .insert(new Suscripcion(encargado, heladeraNueva, MotivoDeDistribucion.FALLA_HELADERA, null));

    return heladeraNueva;
  }

  public void index(Context context) {
    Map<String, Object> model = new HashMap<>();
    List<Heladera> heladeras = new HeladerasRepository().findAll().toList();
    model.put("heladeras", heladeras);
    context.render("contribuciones/cuidado_heladera/cuidado_heladera.hbs", model);
  }

  public void create(Context context) {
    CuidadoHeladeraInputDTO cuidadoHeladeraInputDTO = new CuidadoHeladeraInputDTO(
        context.formParam("nombreHeladera"),
        Integer.parseInt(context.formParam("cantidadViandas")),
        context.sessionAttribute("user_id"),
        Double.parseDouble(context.formParam("latitud")),
        Double.parseDouble(context.formParam("longitud")),
        context.formParam("barrio")
    );
    Heladera heladeraNueva = tomarCuidadoHeladera(cuidadoHeladeraInputDTO);
    new HeladerasRepository().insert(heladeraNueva);
    context.redirect("/");
  }
}
