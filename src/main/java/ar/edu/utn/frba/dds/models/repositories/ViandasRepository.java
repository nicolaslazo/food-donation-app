package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class ViandasRepository extends HibernateEntityManager<Vianda, Long> {
  public Stream<Vianda> findAll(Heladera heladera) {
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Vianda> query = cb.createQuery(Vianda.class);
    Root<Vianda> root = query.from(Vianda.class);

    query.select(root).where(cb.equal(root.get("heladera"), heladera));

    return em.createQuery(query).getResultStream();
  }

  private void assertHeladeraTieneSuficienteEspacio(Heladera destino, int cantidadViandas) {
    final long capacidadDisponible = new HeladerasRepository().getCapacidadDisponible(destino);
    final String sPlural = cantidadViandas > 1 ? "s" : "";

    if (capacidadDisponible < cantidadViandas)
      throw new RuntimeException(
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

  private void assertViandasSonDeLaMismaHeladera(Collection<Vianda> viandas) {
    final Set<Heladera> heladerasInvolucradas = new HashSet<>(viandas.stream().map(Vianda::getHeladera).toList());
    if (heladerasInvolucradas.size() > 1)
      throw new RuntimeException("No se pueden insertar viandas de heladeras distintas en la misma transacción");
  }

  public void insertAll(Collection<Vianda> viandas) {
    // Diseñada para calcular la disponibilidad de espacio en una heladera sola.
    // No hay caso de uso que justifique complicarla
    assertViandasSonDeLaMismaHeladera(viandas);

    final Heladera heladeraInvolucrada = viandas.iterator().next().getHeladera();
    assertHeladeraTieneSuficienteEspacio(heladeraInvolucrada, viandas.size());

    super.insertAll(viandas);
  }

  public void insert(Vianda vianda) {
    if(vianda.getHeladera() != null) {
      assertHeladeraTieneSuficienteEspacio(vianda.getHeladera(), 1);
    }
    super.insert(vianda);
  }

  public void updateUbicacion(Vianda vianda, Heladera ubicacionNueva) {
    assertHeladeraTieneSuficienteEspacio(ubicacionNueva, 1);
    vianda.setHeladera(ubicacionNueva);
    update(vianda);
  }
}
