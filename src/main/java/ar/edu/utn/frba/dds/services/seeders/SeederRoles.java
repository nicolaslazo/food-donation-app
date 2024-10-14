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
    createRoleIfNotExists("COLABORADORFISICO");
    createRoleIfNotExists("COLABORADORJURIDICO");
    createRoleIfNotExists("PERSONAVULNERABLE");
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
          Permiso permisoCrearRecompensa = permisosRepository.findByName("Crear-Recompensas").get();
          Permiso permisoCrearTecnico = permisosRepository.findByName("Crear-Tecnico").get();
          Permiso permisoCuidarHeladera = permisosRepository.findByName("Cuidar-Heladera").get();
          Permiso permisoDonarDinero = permisosRepository.findByName("Donar-Dinero").get();
          Permiso permisoSolicitarTarjetas = permisosRepository.findByName("Solicitar-Tarjetas").get();
          Permiso permisoCrearReporte = permisosRepository.findByName("Crear-Reportes").get();
          Permiso permisoVerReporte = permisosRepository.findByName("Ver-Reportes").get();
          Permiso permisoRegistrarPersonaVulnerable = permisosRepository.findByName("Registrar-Persona-Vulnerable").get();
          Permiso permisoRealizarContribucion = permisosRepository.findByName("Realizar-Contribucion").get();
          Permiso permisoVerAlertas = permisosRepository.findByName("Ver-Alertas").get();
          Permiso permisoCargaCSV = permisosRepository.findByName("Cargar-CSV").get();
          Permiso permisoSuscribirseHeladera = permisosRepository.findByName("Suscribirse-Heladera").get();
          Permiso permisoSubirRecompensa = permisosRepository.findByName("Subir-Recompensa").get();
          Permiso permisoDistribuirViandas = permisosRepository.findByName("Distribuir-Viandas").get();
          Rol newRolAdmin = new Rol(rolName, Set.of(
              permisoCrearColaborador,
              permisoCrearTecnico,
              permisoAsignarTarjetas,
              permisoDarBajaTarjeta,
              permisoCrearTarjetas,
              permisoCuidarHeladera,
              permisoDonarDinero,
              permisoSolicitarTarjetas,
              permisoCrearReporte,
              permisoVerReporte,
              permisoRegistrarPersonaVulnerable,
              permisoRealizarContribucion,
              permisoVerAlertas,
              permisoCargaCSV,
              permisoSuscribirseHeladera,
              permisoSubirRecompensa,
              permisoDistribuirViandas
          ));
          rolesRepository.insert(newRolAdmin);
          break;
        case "TECNICO":
          Rol newRolTecnico = new Rol(rolName, Set.of(
              permisosRepository.findByName("Abrir-Heladera-Contribucion").get()
          ));
          rolesRepository.insert(newRolTecnico);
          break;
        case "COLABORADORFISICO":
          Rol newRolColaboradorFisico = new Rol(rolName, Set.of(
              permisosRepository.findByName("Abrir-Heladera-Contribucion").get(),
              permisosRepository.findByName("Donar-Viandas").get(),
              permisosRepository.findByName("Asignar-Tarjetas").get(),
              permisosRepository.findByName("Depositar-Viandas").get(),
              permisosRepository.findByName("Donar-Dinero").get(),
              permisosRepository.findByName("Solicitar-Tarjetas").get(),
              permisosRepository.findByName("Realizar-Contribucion").get(),
              permisosRepository.findByName("Registrar-Persona-Vulnerable").get(),
              permisosRepository.findByName("Suscribirse-Heladera").get(),
              permisosRepository.findByName("Distribuir-Viandas").get()
          ));
          rolesRepository.insert(newRolColaboradorFisico);
          break;
        case "COLABORADORJURIDICO":
          Rol newRolColaboradorJuridico = new Rol(rolName, Set.of(
              permisosRepository.findByName("Administrar-Recompensas").get(),
              permisosRepository.findByName("Donar-Dinero").get(),
              permisosRepository.findByName("Cuidar-Heladera").get(),
              permisosRepository.findByName("Realizar-Contribucion").get(),
              permisosRepository.findByName("Suscribirse-Heladera").get(),
              permisosRepository.findByName("Subir-Recompensa").get()
          ));
          rolesRepository.insert(newRolColaboradorJuridico);
          break;
        case "PERSONAVULNERABLE":
          Rol newRolPersonaVulnerable = new Rol(rolName, Set.of(
              permisosRepository.findByName("Abrir-Heladera-Consumicion").get()
          ));
          rolesRepository.insert(newRolPersonaVulnerable);
          break;
        default:
          throw new RuntimeException("No se puede crear el rol");
      }

    }
  }
}
