package ar.edu.utn.frba.dds.controllers.contribucion;

import ar.edu.utn.frba.dds.controllers.heladera.HeladeraController;
import ar.edu.utn.frba.dds.controllers.heladera.SolicitudAperturaPorContribucionController;
import ar.edu.utn.frba.dds.dtos.input.contribucion.DonacionViandaInputDTO;
import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.DonacionViandas;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.users.PermisoDenegadoException;
import ar.edu.utn.frba.dds.models.repositories.ViandasRepository;
import ar.edu.utn.frba.dds.models.repositories.contribucion.DonacionViandasRepository;
import ar.edu.utn.frba.dds.models.repositories.documentacion.TarjetasRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.incidente.IncidenteRepository;
import ar.edu.utn.frba.dds.models.repositories.users.UsuariosRepository;
import io.javalin.http.Context;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DonacionViandaController {
  public void registrarDonacionViandas(Tarjeta tarjeta, List<DonacionViandaInputDTO> dtos) throws MqttException, PermisoDenegadoException {
    // Recupero a la Heladera donde se depositaran las Viandas
    long idHeladera = dtos.stream().findFirst().get().getIdHeladera();
    Heladera heladeraDestino = new HeladerasRepository().findById(idHeladera).get();

    /* NOTE: No se settea la Vianda a la Heladera hasta que no ingrese físicamente */
    // Recorro los dtos para Instanciar las Viandas
    Collection<Vianda> viandas = new ArrayList<>();
    for (DonacionViandaInputDTO dto : dtos) {
      Vianda viandaNueva = dto.getVianda();

      viandas.add(viandaNueva);
    }

    ViandasRepository repositorioViandas = new ViandasRepository();
    HeladeraController.assertHeladeraTieneSuficienteEspacio(heladeraDestino, viandas.size());
    repositorioViandas.insertAll(viandas);

    Colaborador colaborador = viandas.iterator().next().getColaborador();

    DonacionViandas donacionViandas = new DonacionViandas(
        colaborador,
        viandas,
        heladeraDestino
    );
    new DonacionViandasRepository().insert(donacionViandas);

    new SolicitudAperturaPorContribucionController().crear(tarjeta, donacionViandas);
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

  public void create(Context context) throws MqttException, PermisoDenegadoException {
    // Recupero el ID de la Heladera donde se depositaran las viandas
    long idHeladera = Long.parseLong(context.formParam("idHeladera"));
    Optional<Heladera> optionalHeladera = new HeladerasRepository().findById(idHeladera);

    if (optionalHeladera.isEmpty()) {
      context.json(new HashMap<>() {
        {
          put("message", "No se encontró la heladera seleccionada");
          put("success", false);
        }
      });

      return;
    }

    if (!new IncidenteRepository().getIsActiva(optionalHeladera.get())) {
      context.json(new HashMap<>() {
        {
          put("message", "Lo sentimos, la heladera seleccionada se encuentra inactiva. Seleccione otra para proceder.");
          put("success", false);
        }
      });

      return;
    }

    Optional<Tarjeta> optionalTarjeta =
        new TarjetasRepository()
            .findActivaByRecipiente(new UsuariosRepository().findById(context.sessionAttribute("user_id")).get());

    if (optionalTarjeta.isEmpty()) {
      context.json(new HashMap<>() {
        {
          put("message", "No se encontró una tarjeta activa para el usuario");
          put("success", false);
        }
      });

      return;
    }

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

    try {
      registrarDonacionViandas(optionalTarjeta.get(), dtos);
    } catch (PermisoDenegadoException e) {
      context.json(new HashMap<>() {
        {
          put("message", e.getMessage());
          put("success", false);
        }
      });

      return;
    }

    context.json(new HashMap<>() {
      {
        put("message", "La heladera está esperando tu(s) vianda(s)! Redirigiendo...");
        put("success", true);
        put("urlRedireccion", "/quiero-ayudar");
        put("demoraRedireccionEnSegundos", 3);
      }
    });
  }
}
