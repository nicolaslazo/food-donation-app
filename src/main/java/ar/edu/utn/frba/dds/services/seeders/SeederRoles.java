package ar.edu.utn.frba.dds.services.seeders;

import ar.edu.utn.frba.dds.models.entities.users.Permiso;
import ar.edu.utn.frba.dds.models.entities.users.Rol;
import ar.edu.utn.frba.dds.models.repositories.users.PermisosRepository;
import ar.edu.utn.frba.dds.models.repositories.users.RolesRepository;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.Set;


public class SeederRoles {
  RolesRepository rolesRepository = new RolesRepository();
  PermisosRepository permisosRepository = new PermisosRepository();
  SeederPermisos seederPermisos = new SeederPermisos();

  @PostConstruct
  public void seedRoles() {
    seederPermisos.seedPermisos();
    createRoleIfNotExists("ADMINISTRADOR");
    createRoleIfNotExists("TECNICO");
    createRoleIfNotExists("COLABORADOR");
  }

  private void createRoleIfNotExists(String rolName) {
    Optional<Rol> rol = rolesRepository.findByName(rolName);
    if (rol.isEmpty()) {
      switch (rolName) {
        case "ADMINISTRADOR":
          Permiso permisoCrearColaborador = permisosRepository.findByName("Crear-Colaborador").get();
          Permiso permisoCrearTecnico = permisosRepository.findByName("Crear-Tecnico").get();
          Permiso permisoCrearTarjetas = permisosRepository.findByName("Crear-Tarjetas").get();
          Permiso permisoAsignarTarjetas = permisosRepository.findByName("Asignar-Tarjetas").get();
          Permiso permisoDarBajaTarjeta = permisosRepository.findByName("Dar-Baja-Tarjetas").get();
          Rol newRolAdmin = new Rol(rolName, Set.of(
                  permisoCrearColaborador,
                  permisoCrearTecnico,
                  permisoAsignarTarjetas,
                  permisoDarBajaTarjeta,
                  permisoCrearTarjetas
          ));
          rolesRepository.insert(newRolAdmin);
          break;
        case "TECNICO":
          Rol newRolTecnico = new Rol(rolName, Set.of(
                  permisosRepository.findByName("Abrir-Heladera").get()
          ));
          rolesRepository.insert(newRolTecnico);
          break;
        case "COLABORADOR":
          Rol newRolColaborador = new Rol(rolName, Set.of(
                  permisosRepository.findByName("Abrir-Heladera").get(),
                  permisosRepository.findByName("Donar-Viandas").get(),
                  permisosRepository.findByName("Asignar-Tarjetas").get(),
                  permisosRepository.findByName("Depositar-Viandas").get()
          ));
          rolesRepository.insert(newRolColaborador);
          break;
        default:
          throw new RuntimeException("No se puede crear el rol");
      }

    }
  }
}
