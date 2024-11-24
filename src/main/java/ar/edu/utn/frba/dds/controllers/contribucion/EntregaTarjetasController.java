package ar.edu.utn.frba.dds.controllers.contribucion;

import ar.edu.utn.frba.dds.dtos.input.contribucion.EntregaTarjetasInputDTO;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.MensajeAContactoException;
import ar.edu.utn.frba.dds.models.entities.contribucion.EntregaTarjetas;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.ubicacion.DireccionResidencia;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.contacto.ContactosRepository;
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
  final String plantillaMailEntregaTarjetas = """
      Usted ha solicitado Tarjetas Alimentarias. En breve le enviaremos las tarjetas.\n
      \n
      Las Tarjetas Alimentarias tendrán los siguientes códigos:\n\n\n
      """;

  public void registrarSolicitudEntregaTarjetas(EntregaTarjetasInputDTO dto) {
    // Recupero al Colaborador que hace la solicitud de las tarjetas
    Colaborador colaboradorSolicitante = new ColaboradorRepository().findById(dto.getIdColaborador()).orElseThrow(() ->
            new RuntimeException("Colaborador no encontrado"));

    // Genero las tarjetas
    List<Tarjeta> tarjetas = generarTarjetas(dto.getTarjetasSolicitadas());

    EntregaTarjetas entregaTarjetas = new EntregaTarjetas(
            colaboradorSolicitante,
            tarjetas
    );

    new EntregaTarjetasRepository().insert(entregaTarjetas);

    // Si el Colaborador ingresó una Dirección, la registramos
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

      notificarAlUsuario(colaboradorSolicitante.getUsuario(), tarjetas);
    }
  }

  private void notificarAlUsuario(Usuario usuario, List<Tarjeta> tarjetas) {
    String mensaje = plantillaMailEntregaTarjetas + generarPlantillaCodigos(tarjetas);

    new ContactosRepository()
            .get(usuario)
            .forEach(contacto -> {
              try {
                contacto.enviarMensaje(mensaje);
              } catch (MensajeAContactoException e) {
                throw new RuntimeException("Error al enviar mensaje al contacto: " + contacto, e);
              }
            });
  }

  private String generarPlantillaCodigos(List<Tarjeta> tarjetas) {
    StringBuilder plantillaCodigos = new StringBuilder();
    int i = 1;
    for (Tarjeta tarjeta : tarjetas) {
      plantillaCodigos.append("\n").append(i).append(". Código: ").append(tarjeta.getId()).append("\n");
      i++;
    }
    return plantillaCodigos.toString();
  }

  public List<Tarjeta> generarTarjetas(int cantidad) {
    List<Tarjeta> tarjetas = new ArrayList<>();
    for (int i = 0; i < cantidad; i++) {
      UUID uuid = UUID.randomUUID();
      Tarjeta tarjeta = new Tarjeta(uuid);

      System.out.println(i + ". UUID Generado: " + uuid);

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
