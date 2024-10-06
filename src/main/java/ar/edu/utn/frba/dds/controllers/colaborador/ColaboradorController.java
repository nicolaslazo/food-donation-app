package ar.edu.utn.frba.dds.controllers.colaborador;

import ar.edu.utn.frba.dds.dtos.input.colaborador.ColaboradorInputDTO;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.users.PermisoDenegadoException;
import ar.edu.utn.frba.dds.models.entities.users.Rol;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.users.RolesRepository;
import io.javalin.http.Context;

public class ColaboradorController {
  ColaboradorRepository colaboradorRepository = new ColaboradorRepository();
  RolesRepository rolesRepository = new RolesRepository();

  public void index(Context context) {
    context.render("logueo/registro/registro.hbs");
  }

  public void crearColaborador(ColaboradorInputDTO data, Usuario usuarioCreador) throws PermisoDenegadoException {
    usuarioCreador.assertTienePermiso(
            "Crear-Colaborador",
            "No tienes permisos para crear un nuevo colaborador"
    );

    Rol rolColaborador = rolesRepository.findByName("COLABORADOR").get();
    Colaborador newColaborador = new Colaborador(
            data.getDocumento(),
            data.getPrimerNombre(),
            data.getApellido(),
            data.getFechaNacimiento(),
            data.getCoordenadasGeograficas(),
            rolColaborador);
    colaboradorRepository.insert(newColaborador);
  }
}
