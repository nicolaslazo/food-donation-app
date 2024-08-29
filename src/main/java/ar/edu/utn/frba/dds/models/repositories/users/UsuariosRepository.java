package ar.edu.utn.frba.dds.models.repositories.users;

import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;

import java.util.UUID;

public class UsuariosRepository extends HibernateEntityManager<Usuario, UUID> {
  static UsuariosRepository instancia = null;

  public static UsuariosRepository getInstancia() {
    if (instancia == null) instancia = new UsuariosRepository();

    return instancia;
  }
}
