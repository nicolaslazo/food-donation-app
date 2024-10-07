package ar.edu.utn.frba.dds.controllers.tecnico;

import ar.edu.utn.frba.dds.dtos.input.tecnico.TecnicoInputDTO;
import ar.edu.utn.frba.dds.models.entities.Tecnico;
import ar.edu.utn.frba.dds.models.entities.users.PermisoDenegadoException;
import ar.edu.utn.frba.dds.models.entities.users.Rol;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.TecnicoRepository;
import ar.edu.utn.frba.dds.models.repositories.users.PermisosRepository;
import ar.edu.utn.frba.dds.models.repositories.users.RolesRepository;


public class TecnicoController {
  TecnicoRepository tecnicoRepository = new TecnicoRepository();
  PermisosRepository permisosRepository = new PermisosRepository();
  RolesRepository rolesRepository = new RolesRepository();

  public void crearTecnico(TecnicoInputDTO data, Usuario usuarioCreador) throws PermisoDenegadoException {
    usuarioCreador.assertTienePermiso(
            "Crear-Tecnico",
            "No tienes permisos para crear un nuevo tecnico"
    );

    Rol rolTecnico = rolesRepository.findByName("TECNICO").get();
    Tecnico newTecnico = new Tecnico(
            data.getDocumento(),
            data.getPrimerNombre(),
            data.getApellido(),
            data.getFechaNacimiento(),
            data.getCuil(),
            data.getAreaGeografica(),
            data.getContrasenia(),
            rolTecnico
    );
    tecnicoRepository.insert(newTecnico);
  }
}
