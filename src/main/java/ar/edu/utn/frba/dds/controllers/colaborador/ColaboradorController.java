package ar.edu.utn.frba.dds.controllers.colaborador;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Email;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.ubicacion.DireccionResidencia;
import ar.edu.utn.frba.dds.models.entities.users.TipoPersonaJuridica;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.contacto.ContactosRepository;
import ar.edu.utn.frba.dds.models.repositories.ubicacion.DireccionResidenciaRepository;
import ar.edu.utn.frba.dds.models.repositories.users.PermisosRepository;
import ar.edu.utn.frba.dds.models.repositories.users.RolesRepository;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.util.Map;

public class ColaboradorController {
  public void index(Context context) {
    Map<String, Object> model = context.attribute("model");
    context.render("logueo/registro/registro.hbs", model);
  }

  public void create(Context context) {
    Colaborador colaborador;
    String documento = context.formParam("documento");

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
    } else { // Es una persona jurídica
      colaborador = new Colaborador(
          new Documento(TipoDocumento.CUIT, Integer.parseInt(context.formParam("cuit"))),
          TipoPersonaJuridica.fromString(context.formParam("tipoPersonaJuridica")),
          context.formParam("primerNombre"),
          // LocalDate.parse(context.formParam("fechaCreacion")),  TODO: No tenemos un campo para la fecha de creación
          null,
          context.formParam("password")
      );
    }

    new ColaboradorRepository().insert(colaborador);

    Email email = new Email(colaborador.getUsuario(), context.formParam("email"));
    new ContactosRepository().insert(email);

    String pais = context.formParam("pais");
    if (pais != null && !pais.isBlank()) {  // Hay una dirección definida
      DireccionResidencia residencia = new DireccionResidencia(
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
      new DireccionResidenciaRepository().insert(residencia);
    }

    context.sessionAttribute("user_id", colaborador.getId());
    context.sessionAttribute("permisos",
        new PermisosRepository().findAll(colaborador.getUsuario()).toList());
    context.redirect("/");
  }
}
