package ar.edu.utn.frba.dds.controllers.contacto;

import ar.edu.utn.frba.dds.dtos.input.FormularioDeContactoDTO;
import ar.edu.utn.frba.dds.models.entities.FormularioContacto;
import ar.edu.utn.frba.dds.models.repositories.FormularioContactoRepository;
import io.javalin.http.Context;

public class ContactoController {

  public void index(Context context) {
    context.render("contacto/contacto.hbs");
  }

  private FormularioContacto convertirDTOAEntidad(FormularioDeContactoDTO dto) {
    FormularioContacto entidad = new FormularioContacto();
    entidad.setName(dto.getNombre());
    entidad.setEmail(dto.getEmail());
    entidad.setSubject(dto.getSubject());
    entidad.setMessage(dto.getMensaje());
    return entidad;
  }
  public void create(Context context) {
    FormularioDeContactoDTO formularioDeContactoDTO = new FormularioDeContactoDTO(
        context.formParam("nombre"),
        context.formParam("email"),
        context.formParam("subject"),
        context.formParam("mensaje")
    );

    new FormularioContactoRepository().insert(convertirDTOAEntidad(formularioDeContactoDTO));
    context.redirect("/contacto");
  }
}
