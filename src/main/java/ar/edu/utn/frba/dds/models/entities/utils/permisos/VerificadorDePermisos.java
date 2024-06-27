package ar.edu.utn.frba.dds.models.entities.utils.permisos;

import ar.edu.utn.frba.dds.models.entities.users.Permiso;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.users.IPermisosRepository;

import java.util.Optional;

public class VerificadorDePermisos {
  private final IPermisosRepository permisosRepository;

  public VerificadorDePermisos(IPermisosRepository permisosRepository) {
    this.permisosRepository = permisosRepository;
  }

  public void verificarSiUsuarioPuede(String accion, Usuario usuario) {
    Optional<Permiso> permisoBuscado = this.permisosRepository.buscarPermisoPorNombre(accion);

    if (permisoBuscado.isEmpty())
      throw new RuntimeException("No existe un permiso con el nombre " + accion);

    Permiso permiso = permisoBuscado.get();

    if (!usuario.getRol().tenesPermiso(permiso))
      throw new SinPermisoSuficienteException("Usted no tiene permiso: " + accion);
  }
}

