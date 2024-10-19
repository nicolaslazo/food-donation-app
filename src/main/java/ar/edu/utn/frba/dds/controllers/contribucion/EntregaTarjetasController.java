package ar.edu.utn.frba.dds.controllers.contribucion;

import ar.edu.utn.frba.dds.dtos.input.contribucion.EntregaTarjetasInputDTO;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.EntregaTarjetas;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.ubicacion.DireccionResidencia;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.contribucion.EntregaTarjetasRepository;
import ar.edu.utn.frba.dds.models.repositories.documentacion.TarjetasRepository;
import ar.edu.utn.frba.dds.models.repositories.ubicacion.DireccionResidenciaRepository;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class EntregaTarjetasController {
  public void registrarSolicitudEntregaTarjetas(EntregaTarjetasInputDTO dto) {
    // Recupero al Colaborador que hace la solicitud de las tarjetas
    Colaborador colaboradorSolicitante = new ColaboradorRepository().findById(dto.getIdColaborador()).get();

    // Genero las tarjetas
    List<Tarjeta> tarjetas = generarTarjetas(dto.getTarjetasSolicitadas());

    EntregaTarjetas entregaTarjetas = new EntregaTarjetas(
            colaboradorSolicitante,
            tarjetas
    );

    new EntregaTarjetasRepository().insert(entregaTarjetas);
    // Si el Colaborador ingreso una Dirección, la registramos
    if (dto.getPais() != null) {
      DireccionResidencia direccionResidencia = new DireccionResidencia(
              colaboradorSolicitante.getUsuario(),
              dto.getUnidad(),
              dto.getPiso(),
              dto.getAltura(),
              dto.getCalle(),
              dto.getCodigoPostal(),
              dto.getCiudad(),
              dto.getProvincia(),
              dto.getPais()
      );

      Optional<DireccionResidencia> direccionOpcional = new DireccionResidenciaRepository().findById(dto.getIdColaborador());
      direccionOpcional.ifPresent(residencia -> new DireccionResidenciaRepository().delete(residencia));
      new DireccionResidenciaRepository().insert(direccionResidencia);
    }
  }

  public List<Tarjeta> generarTarjetas(int cantidad) {
    List<Tarjeta> tarjetas = new ArrayList<>();
    // Genero la cantidad solicitada de tarjetas
    for (int i = 0; i < cantidad; i++) {
      Tarjeta tarjeta = new Tarjeta(UUID.randomUUID());
      new TarjetasRepository().insert(tarjeta);
      tarjetas.add(tarjeta);
    }
    return tarjetas;
  }

  public void index(Context context) {
    Map<String, Object> model = context.attribute("model");

    context.render("contribuciones/entrega_tarjetas/entrega_tarjetas.hbs", model);
  }

  public void create(Context context) {
    EntregaTarjetasInputDTO dto = new EntregaTarjetasInputDTO(
            context.sessionAttribute("user_id"),
            Integer.parseInt(context.formParam("cantidadTarjetas")),
            context.formParam("pais"),
            context.formParam("provincia"),
            context.formParam("ciudad"),
            context.formParam("codigoPostal"),
            context.formParam("calle"),
            context.formParam("altura"),
            context.formParam("unidad"),
            context.formParam("piso")
    );
    registrarSolicitudEntregaTarjetas(dto);
    context.redirect("/quiero-ayudar");
  }
}
