package ar.edu.utn.frba.dds.services.seeders;

import ar.edu.utn.frba.dds.models.entities.users.Permiso;
import ar.edu.utn.frba.dds.models.entities.users.Rol;
import ar.edu.utn.frba.dds.models.repositories.users.PermisosRepository;
import ar.edu.utn.frba.dds.models.repositories.users.RolesRepository;

import javax.annotation.PostConstruct;
import java.util.Set;


public class SeederRoles {
  RolesRepository rolesRepository = new RolesRepository();
  PermisosRepository permisosRepository = new PermisosRepository();
  SeederPermisos seederPermisos = new SeederPermisos();

  @PostConstruct
  public void seedRoles() {
    seederPermisos.seedPermisos();
    createRoles();
  }

  private void createRoles() {
    // OBTENGO LOS PERMISOS
    Permiso permisoCrearColaborador = permisosRepository.findByName("Crear-Colaborador").get();
    Permiso permisoCrearTecnico = permisosRepository.findByName("Crear-Tecnico").get();
    Permiso permisoCrearTarjetas = permisosRepository.findByName("Crear-Tarjetas").get();
    Permiso permisoAsignarTarjetas = permisosRepository.findByName("Asignar-Tarjetas").get();
    Permiso permisoDarBajaTarjeta = permisosRepository.findByName("Dar-Baja-Tarjetas").get();
    Permiso permisoAbrirHeladera = permisosRepository.findByName("Abrir-Heladera").get();
    Permiso permisoDonarVianda = permisosRepository.findByName("Donar-Viandas").get();
    Permiso permisoDepositarVianda = permisosRepository.findByName("Depositar-Viandas").get();

    // ROL ADMINISTRADOR
    Rol newRolAdmin = new Rol("ADMINITRADOR", Set.of(
        permisoCrearColaborador,
        permisoCrearTecnico,
        permisoAsignarTarjetas,
        permisoDarBajaTarjeta,
        permisoCrearTarjetas
    ));
    rolesRepository.insert(newRolAdmin);

    // ROL TECNICO
    Rol newRolTecnico = new Rol("TECNICO", Set.of(
        permisoAbrirHeladera
    ));
    rolesRepository.insert(newRolTecnico);

    // ROL COLABORADOR
    Rol newRolColaborador = new Rol("COLABORADOR", Set.of(
        permisoAbrirHeladera,
        permisoDonarVianda,
        permisoAsignarTarjetas,
        permisoDepositarVianda
    ));
    rolesRepository.insert(newRolColaborador);
  }
}
