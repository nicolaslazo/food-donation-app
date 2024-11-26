package ar.edu.utn.frba.dds.controllers.colaborador;

import ar.edu.utn.frba.dds.controllers.documentacion.TarjetaController;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Email;
import ar.edu.utn.frba.dds.models.entities.contacto.MensajeAContactoException;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.ubicacion.DireccionResidencia;
import ar.edu.utn.frba.dds.models.entities.users.PermisoDenegadoException;
import ar.edu.utn.frba.dds.models.entities.users.TipoPersonaJuridica;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.contacto.ContactosRepository;
import ar.edu.utn.frba.dds.models.repositories.documentacion.TarjetasRepository;
import ar.edu.utn.frba.dds.models.repositories.ubicacion.DireccionResidenciaRepository;
import ar.edu.utn.frba.dds.models.repositories.users.PermisosRepository;
import ar.edu.utn.frba.dds.models.repositories.users.RolesRepository;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public class ColaboradorController {
  final String plantillaMailEntregaTarjetas = """
      ¡Bienvenido a Viandas Donation!\n 
      \n
      Muchas gracias por sumarte a colaborar con nosotros. Tu apoyo es invaluable para llevar alimentos a quienes más lo necesitan.\n
      \n
      Si tienes alguna pregunta o necesitas más información, no dudes en contactarnos.\n
      \n
      ¡Gracias por ser parte de esta causa!
      """;

  public void index(Context context) {
    Map<String, Object> model = context.attribute("model");
    context.render("logueo/registro/registro.hbs", model);
  }

  public void create(Context context) {
    try {
      Colaborador colaborador;
      String documento = context.formParam("documento");

      String emailString;
      if (documento != null && !documento.isBlank()) { // Es una persona física
        colaborador = new Colaborador(
                new Documento(TipoDocumento.fromString(context.formParam("tipoDocumento")), Integer.parseInt(documento)),
                context.formParam("primerNombre"),
                context.formParam("apellido"),
                LocalDate.parse(context.formParam("fechaNacimiento")),
                null,
                context.formParam("password"),
                new RolesRepository().findByName("COLABORADORFISICO").get()
        );
        emailString = context.formParam("email");
      } else { // Es una persona jurídica
        colaborador = new Colaborador(
                new Documento(TipoDocumento.CUIT, Integer.parseInt(context.formParam("cuit"))),
                TipoPersonaJuridica.fromString(context.formParam("tipoPersonaJuridica")),
                context.formParam("razonSocial"),
                // LocalDate.parse(context.formParam("fechaCreacion")),  TODO: No tenemos un campo para la fecha de creación
                null,
                context.formParam("password")
        );
        emailString = context.formParam("emailJuridico");
      }

      new ColaboradorRepository().insert(colaborador);

      Email email = new Email(colaborador.getUsuario(), emailString);
      new ContactosRepository().insert(email);

      DireccionResidencia direccion = parsearDireccionResidencia(context, colaborador);
      new DireccionResidenciaRepository().insert(direccion);

      context.sessionAttribute("user_id", colaborador.getId());
      context.sessionAttribute("permisos",
              new PermisosRepository().findAll(colaborador.getUsuario()).toList());

      if (colaborador.getUsuario().tienePermiso("Abrir-Heladera-Contribucion")) {
        asignarTarjetaAlimentaria(colaborador.getUsuario());
      } else {
        notificarColaborador(colaborador.getUsuario(), plantillaMailEntregaTarjetas);
      }

      context.json(Map.of(
          "message", "Cuenta registrada con éxito! Redirigiendo en tres segundos...",
          "success", true,
          "urlRedireccion", "/colaborador/login",
          "demoraRedireccionEnSegundos", 3
      ));
    } catch (Exception e) {
      context.json(Map.of(
          "message", "Contraseña incorrecta o usuario no encontrado",
          "success", false
      ));
    }
  }

  private DireccionResidencia parsearDireccionResidencia(Context context, Colaborador colaborador) {
    String pais = context.formParam("pais");

    if (pais == null || pais.isBlank()) return null;

    return new DireccionResidencia(
        colaborador.getUsuario(),
        context.formParam("unidad"),
        context.formParam("piso"),
        context.formParam("altura"),
        context.formParam("calle"),
        context.formParam("codigoPostal"),
        context.formParam("ciudad"),
        context.formParam("provincia"),
        pais
    );
  }

  private void asignarTarjetaAlimentaria(Usuario usuario) {
    try {
      final String plantillaCodigoTarjeta = """ 
          En los próximos días hábiles recibirás en tu domicilio una Tarjeta Alimentaria.\n
          \n
          Esta tarjeta te permitirá abrir las heladeras para colaborar con donaciones de viandas o participar en la redistribución de alimentos. 
          Todas las tarjetas cuentan con un código único para identificar tu colaboración.\n
          \n
          Tu código es: %s \n
          \n
          Si tienes dudas sobre cómo utilizar tu tarjeta, por favor contáctanos. ¡Gracias por tu solidaridad!
          """;

      UUID uuid = UUID.randomUUID();
      Tarjeta tarjeta = new Tarjeta(uuid);

      new TarjetasRepository().insert(tarjeta);

      TarjetaController.darDeAlta(tarjeta, usuario);

      String mensajeMail = plantillaMailEntregaTarjetas + "\n" + plantillaCodigoTarjeta.formatted(uuid.toString());

      notificarColaborador(usuario, mensajeMail);
    } catch (PermisoDenegadoException e) {
      System.out.println(e.getMessage());
    }
  }

  public void notificarColaborador(Usuario usuario, String mensaje) {
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
}
