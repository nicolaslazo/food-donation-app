package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.Tecnico;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import lombok.Getter;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Getter
public class TecnicoRepository extends HibernateEntityManager<Tecnico, UUID> {
  static TecnicoRepository instancia = null;

  public static TecnicoRepository getInstancia() {
    if (instancia == null) {
      instancia = new TecnicoRepository();
    }

    return instancia;
  }

  @Override
  public Stream<Tecnico> findAll() {
    Stream<Tecnico> tecnicos = super.findAll();
    return tecnicos.filter(e -> e.getUsuario().getActive() == true);
  }

  @SuppressWarnings("unchecked")
  public Optional<Tecnico> searchBy(String nameColumn, String value) {
    return entityManager()
            .createQuery("from " + claseDeEntidad.getSimpleName() + " where cuil =:cuil")
            .setParameter("cuil", value)
            .getResultStream().findFirst();
  }

  public void delete(String cuil) {
    Optional<Tecnico> tecnico = this.searchBy("cuil", cuil);
    if (tecnico.isPresent()) {
      Tecnico myTecnico = tecnico.get();
      Usuario myUsuario = myTecnico.getUsuario();
      myUsuario.setActive(false);
      this.update(myTecnico);
    }
  }
}
