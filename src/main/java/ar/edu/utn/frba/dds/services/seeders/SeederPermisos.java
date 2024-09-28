package ar.edu.utn.frba.dds.services.seeders;

import ar.edu.utn.frba.dds.models.entities.users.Permiso;
import ar.edu.utn.frba.dds.models.repositories.users.PermisosRepository;

import javax.annotation.PostConstruct;
import java.util.Optional;

public class SeederPermisos {
  PermisosRepository permisosRepository = new PermisosRepository();

  @PostConstruct
  public void seedPermisos() {
    permisosRepository.insert(new Permiso("Abrir-Heladera"));
    permisosRepository.insert(new Permiso("Asignar-Tarjetas"));
    permisosRepository.insert(new Permiso("Crear-Colaborador"));
    permisosRepository.insert(new Permiso("Crear-Tecnico"));
    permisosRepository.insert(new Permiso("Crear-Tarjetas"));
    permisosRepository.insert(new Permiso("Dar-Baja-Tarjetas"));
    permisosRepository.insert(new Permiso("Depositar-Viandas"));
    permisosRepository.insert(new Permiso("Donar-Viandas"));
  }
}
