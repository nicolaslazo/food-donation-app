package ar.edu.utn.frba.dds.models.repositories.contacto;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Suscripcion;
import ar.edu.utn.frba.dds.models.entities.contribucion.MotivoDeDistribucion;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import lombok.NonNull;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class SuscripcionRepository extends HibernateEntityManager<Suscripcion, Long> {
  public Optional<Suscripcion> find(@NonNull Colaborador colaborador,
                                    @NonNull Heladera heladera,
                                    @NonNull MotivoDeDistribucion tipo) {
    EntityManager em = entityManager();

    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Suscripcion> query = cb.createQuery(Suscripcion.class);
    Root<Suscripcion> root = query.from(Suscripcion.class);

    List<Predicate> predicates = new ArrayList<>();

    predicates.add(cb.equal(root.get("heladera"), heladera));
    predicates.add(cb.equal(root.get("tipo"), tipo));
    predicates.add(cb.equal(root.get("colaborador"), colaborador));

    query.where(predicates.toArray(new Predicate[0]));

    return Optional.ofNullable(em.createQuery(query).getSingleResult());
  }

  /* Dada una heladera, busca todas las suscripciones que deberían recibir una notificación relacionada a stock */
  public Stream<Suscripcion> findInteresadasEnStock(@NonNull Heladera heladera) {
    int capacidadTotal = heladera.getCapacidadEnViandas();
    long cantidadViandasDepositadas = new HeladerasRepository().getCantidadViandasDepositadas(heladera);

    EntityManager em = entityManager();

    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Suscripcion> query = cb.createQuery(Suscripcion.class);
    Root<Suscripcion> root = query.from(Suscripcion.class);

    Predicate heladeraPredicate = cb.equal(root.get("heladera"), heladera);

    Predicate faltanViandasPredicate = cb.and(
        cb.equal(root.get("tipo"), MotivoDeDistribucion.FALTAN_VIANDAS),
        cb.lessThan(cb.literal(cantidadViandasDepositadas), root.get("parametro"))
    );

    Predicate faltaEspacioPredicate = cb.and(
        cb.equal(root.get("tipo"), MotivoDeDistribucion.FALTA_ESPACIO),
        cb.lessThan(cb.literal(capacidadTotal - cantidadViandasDepositadas), root.get("parametro"))
    );

    query.where(
        cb.and(
            heladeraPredicate,
            cb.or(faltanViandasPredicate, faltaEspacioPredicate)
        )
    );

    return em.createQuery(query).getResultStream();
  }

  public Stream<Suscripcion> findAll(Heladera heladera, MotivoDeDistribucion tipo) {
    EntityManager em = entityManager();

    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Suscripcion> query = cb.createQuery(Suscripcion.class);
    Root<Suscripcion> root = query.from(Suscripcion.class);

    Predicate heladeraPredicate = cb.equal(root.get("heladera"), heladera);
    Predicate tipoPredicate = cb.equal(root.get("tipo"), tipo);

    query.where(cb.and(heladeraPredicate, tipoPredicate));

    return em.createQuery(query).getResultStream();
  }
}
