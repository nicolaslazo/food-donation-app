package ar.edu.utn.frba.dds.services.seeders;

import ar.edu.utn.frba.dds.models.entities.users.Permiso;
import ar.edu.utn.frba.dds.models.repositories.users.PermisosRepository;

import javax.annotation.PostConstruct;
import java.util.Optional;

public class SeederPermisos {
  PermisosRepository permisosRepository = new PermisosRepository();

  @PostConstruct
  public void seedPermisos() {
    createPermisoIfNotExists("Abrir-Heladera-Contribucion");
    createPermisoIfNotExists("Abrir-Heladera-Consumicion");
    createPermisoIfNotExists("Crear-Colaborador");
    createPermisoIfNotExists("Crear-Tecnico");
    createPermisoIfNotExists("Donar-Viandas");
    createPermisoIfNotExists("Crear-Tarjetas");
    createPermisoIfNotExists("Asignar-Tarjetas");
    createPermisoIfNotExists("Dar-Baja-Tarjetas");
    createPermisoIfNotExists("Depositar-Viandas");
    createPermisoIfNotExists("Administrar-Recompensas");
    createPermisoIfNotExists("Crear-Recompensas");
    createPermisoIfNotExists("Cuidar-Heladera");
    createPermisoIfNotExists("Solicitar-Tarjetas");
  }

  public void createPermisoIfNotExists(String nombrePermiso) {
    Optional<Permiso> permiso = permisosRepository.findByName(nombrePermiso);
    if (permiso.isEmpty()) {
      Permiso newPermiso = new Permiso(nombrePermiso);
      permisosRepository.insert(newPermiso);
    }
  }
}