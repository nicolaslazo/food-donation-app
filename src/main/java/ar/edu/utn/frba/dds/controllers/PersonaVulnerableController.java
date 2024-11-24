package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.models.entities.PersonaVulnerable;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Email;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.ubicacion.DireccionResidencia;
import ar.edu.utn.frba.dds.models.entities.users.PermisoDenegadoException;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.PersonaVulnerableRepository;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.contacto.ContactosRepository;
import ar.edu.utn.frba.dds.models.repositories.documentacion.TarjetasRepository;
import ar.edu.utn.frba.dds.models.repositories.ubicacion.DireccionResidenciaRepository;
import ar.edu.utn.frba.dds.models.repositories.users.RolesRepository;
import io.javalin.http.Context;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PersonaVulnerableController {
  public void index(Context context) {
    Map<String, Object> model = context.attribute("model");

    context.render("/personavulnerable/carga.hbs", model);
  }

  public void create(Context context) throws PermisoDenegadoException {
    Optional<Tarjeta> optionalTarjetaAsignada =
        new TarjetasRepository().findById(UUID.fromString(context.formParam("tarjeta")));
    if (optionalTarjetaAsignada.isEmpty()) {
      context.json(new HashMap<>() {
        {
          put("message", "No existe una tarjeta con ese id");
          put("success", false);
        }
      });

      return;
    }

    Tarjeta tarjeta = optionalTarjetaAsignada.get();
    if (tarjeta.getFechaAlta() != null) {
      context.json(new HashMap<>() {
        {
          put("message", "Esta tarjeta ya fue dada de alta");
          put("success", false);
        }
      });

      return;
    }

    Documento documento = Boolean.parseBoolean(context.formParam("tiene-dni")) ?
        new Documento(
            TipoDocumento.fromString(context.formParam("documento")),
            Integer.parseInt(context.formParam("numero-documento")))
        : null;
    Usuario usuario = new Usuario(
        documento,
        context.formParam("name"),
        context.formParam("surname"),
        LocalDate.parse(context.formParam("dob")),
        DigestUtils.sha256Hex(context.formParam("password")),
        new HashSet<>(List.of(new RolesRepository().findByName("PERSONAVULNERABLE").get()))
    );
    DireccionResidencia residencia = Boolean.parseBoolean(context.formParam("tiene-domicilio")) ?
        new DireccionResidencia(
            usuario,
            context.formParam("unidad"),
            context.formParam("piso"),
            context.formParam("altura"),
            context.formParam("calle"),
            context.formParam("codigoPostal"),
            context.formParam("ciudad"),
            context.formParam("provincia"),
            context.formParam("pais"))
        : null;
    Colaborador reclutador = new ColaboradorRepository().findById(context.sessionAttribute("user_id")).get();
    PersonaVulnerable personaVulnerable = new PersonaVulnerable(
        usuario,
        reclutador,
        ZonedDateTime.now(),
        residencia,
        Integer.parseInt(context.formParam("menores-cargo"))
    );

    tarjeta.setEnAlta(usuario, reclutador, ZonedDateTime.now());

    new PersonaVulnerableRepository().insert(personaVulnerable);
    if (residencia != null) new DireccionResidenciaRepository().insert(residencia);
    new ContactosRepository().insert(new Email(usuario, context.formParam("email")));
    new TarjetasRepository().update(tarjeta);

//    context.redirect("/quiero-ayudar", HttpStatus.OK);
    context.json(new HashMap<>() {
      {
        put("message", "Persona vulnerable registrada con éxito! Redirigiendo...");
        put("success", true);
        put("urlRedireccion", "/quiero-ayudar");
        put("demoraRedireccionEnSegundos", 3);
      }
    });
  }
}
