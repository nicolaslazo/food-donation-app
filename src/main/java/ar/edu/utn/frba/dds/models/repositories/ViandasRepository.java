package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

//TODO Este repo tiene mucha logica, capaz conviene trabajarlo directo en un controller

public class ViandasRepository extends HibernateEntityManager<Vianda, Long> {

  public Optional<Vianda> get(Long id) {
    return findById(id);
  }

  public List<Vianda> getTodas() {
    return findAll().toList();
  }

  public List<Vianda> getAlmacenadas(Heladera heladera) {
    //TODO: Cambiar el ID de Heladera en su issue
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Vianda> query = cb.createQuery(Vianda.class);
    Root<Vianda> root = query.from(Vianda.class);

    query.select(root).where(cb.equal(root.get("heladera"), heladera));

    return em.createQuery(query).getResultList();
  }

  private void assertHeladeraTieneSuficienteEspacio(Heladera destino, int cantidadViandas) throws RepositoryException {
    //TODO: Llevarlo al controller
    final int capacidadDisponible = HeladerasRepository.getInstancia().getCapacidadDisponible(destino);
    final String sPlural = cantidadViandas > 1 ? "s" : "";

    if (capacidadDisponible < cantidadViandas)
      throw new RepositoryException(
          "La heladera con id " +
              destino.getId() +
              " no posee el espacio para esta" +
              sPlural +
              " vianda" +
              sPlural +
              ". Requerido: " +
              cantidadViandas +
              ". Disponible: " +
              capacidadDisponible);
  }

  public void insert(Collection<Vianda> viandas) throws RepositoryException {
    // Diseñada para calcular la disponibilidad de espacio en una heladera sola.
    // No hay caso de uso que justifique complicarla
    assertViandasSonDeLaMismaHeladera(viandas);

    //TODO: eventualmente cambiara
    final Heladera heladeraInvolucrada = viandas.iterator().next().getHeladera();
    assertHeladeraTieneSuficienteEspacio(heladeraInvolucrada, viandas.size());

    insertAll(viandas);
  }

  public void insert(Vianda vianda) {
    //TODO: Esto rompe aca, hay que agregar la logica de esto en el controller
    // assertHeladeraTieneSuficienteEspacio(vianda.getHeladera(), 1);
    super.insert(vianda);
  }

  public void update(Vianda vianda) {
    super.update(vianda);
  }

  public void updateUbicacion(Vianda vianda, Heladera ubicacionNueva) throws RepositoryException {
    assertHeladeraTieneSuficienteEspacio(ubicacionNueva, 1);
    vianda.setHeladera(ubicacionNueva);
    update(vianda);
  }

  public void delete(Vianda vianda) {
    super.delete(vianda);
  }

  public void deleteTodas() {
    deleteAll();
  }

  private void assertViandasSonDeLaMismaHeladera(Collection<Vianda> viandas) throws RepositoryException {
    final Set<Heladera> heladerasInvolucradas = new HashSet<>(viandas.stream().map(Vianda::getHeladera).toList());
    if (heladerasInvolucradas.size() > 1)
      throw new RepositoryException("No se pueden insertar viandas de heladeras distintas en la misma transacción");
  }

  // TODO: Necesitamos este método? Lo podemos aplicar en las concretizaciones de contribuciones?
  public void updateUbicacion(Collection<Vianda> viandas, Heladera ubicacionNueva) throws RepositoryException {
    // No hay ninguna razón por la que podamos mover viandas de una heladera a otra
    // afuera del marco de las contribuciones, donde todas las viandas tienen el mismo origen
    assertViandasSonDeLaMismaHeladera(viandas);
    assertHeladeraTieneSuficienteEspacio(ubicacionNueva, viandas.size());

    for (Vianda vianda : viandas) {
      updateUbicacion(vianda, ubicacionNueva);
    }
  }

}
