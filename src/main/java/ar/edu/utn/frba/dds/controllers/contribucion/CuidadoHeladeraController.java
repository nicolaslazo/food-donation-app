package ar.edu.utn.frba.dds.controllers.contribucion;

import ar.edu.utn.frba.dds.controllers.sensor.SensorController;
import ar.edu.utn.frba.dds.dtos.input.contribucion.CuidadoHeladeraInputDTO;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Suscripcion;
import ar.edu.utn.frba.dds.models.entities.contribucion.CuidadoHeladera;
import ar.edu.utn.frba.dds.models.entities.contribucion.MotivoDeDistribucion;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.ModeloHeladera;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.contacto.SuscripcionRepository;
import ar.edu.utn.frba.dds.models.repositories.contribucion.CuidadoHeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import io.javalin.http.Context;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.time.ZonedDateTime;
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
        dtoCuidado.getModeloHeladera(),
        ZonedDateTime.now(),
        dtoCuidado.getBarrio());

    // Se registra la contribución
    CuidadoHeladera cuidadoHeladera = new CuidadoHeladera(
            encargado,
            heladeraNueva
    );

    new HeladerasRepository().insert(heladeraNueva);
    new CuidadoHeladerasRepository().insert(cuidadoHeladera);
    new SuscripcionRepository()
        .insert(new Suscripcion(encargado, heladeraNueva, MotivoDeDistribucion.FALLA_HELADERA, null));

    return heladeraNueva;
  }

  public static Heladera tomarCuidadoHeladera(CuidadoHeladeraInputDTO dtoCuidado) {
    Colaborador encargado = new ColaboradorRepository().findById(dtoCuidado.getIdColaborador()).get();

    Heladera heladeraNueva = new Heladera(dtoCuidado.getNombreHeladera(),
        dtoCuidado.getUbicacion(),
        encargado,
        dtoCuidado.getModeloHeladera(),
        ZonedDateTime.now(),
        dtoCuidado.getBarrio());

    // Se registra la contribución
    CuidadoHeladera cuidadoHeladera = new CuidadoHeladera(
            encargado,
            heladeraNueva
    );

    new HeladerasRepository().insert(heladeraNueva);
    new CuidadoHeladerasRepository().insert(cuidadoHeladera);
    new SuscripcionRepository()
        .insert(new Suscripcion(encargado, heladeraNueva, MotivoDeDistribucion.FALLA_HELADERA, null));

    return heladeraNueva;
  }

  public void index(Context context) {
    Map<String, Object> model = context.attribute("model");
    List<Heladera> heladeras = new HeladerasRepository().findAll().toList();
    model.put("heladeras", heladeras);
    context.render("contribuciones/cuidado_heladera/cuidado_heladera.hbs", model);
  }

  public void create(Context context) throws MqttException {
    // Obtengo los datos del formulario
    CuidadoHeladeraInputDTO cuidadoHeladeraInputDTO = new CuidadoHeladeraInputDTO(
        context.formParam("nombreHeladera"),
        ModeloHeladera.valueOf(context.formParam("modelo").toUpperCase()),
        context.sessionAttribute("user_id"),
        Double.parseDouble(context.formParam("latitud")),
        Double.parseDouble(context.formParam("longitud")),
        context.formParam("barrio")
    );

    // Instancio y Registro la Nueva Heladera
    Heladera heladeraNueva = tomarCuidadoHeladera(cuidadoHeladeraInputDTO);
    new HeladerasRepository().insert(heladeraNueva);

    // Asigno los Sensores a la Heladera
    asignarSensoresAHeladera(heladeraNueva);

    context.redirect("/formas-colaboracion");
  }

  private void asignarSensoresAHeladera(Heladera heladera) throws MqttException {
    new SensorController().create(heladera);
  }
}
