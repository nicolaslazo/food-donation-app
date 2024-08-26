package ar.edu.utn.frba.dds.models.repositories.users;

import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;

public class UsuariosRepository extends HibernateEntityManager<Usuario> {
  static UsuariosRepository instancia = null;

  public static UsuariosRepository getInstancia() {
    if (instancia == null) instancia = new UsuariosRepository();

    return instancia;
  }
}
