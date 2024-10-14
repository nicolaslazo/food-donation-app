package ar.edu.utn.frba.dds.models.repositories.recompensas;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.recompensas.CalculadoraDePuntos;
import ar.edu.utn.frba.dds.models.entities.recompensas.Canjeo;
import ar.edu.utn.frba.dds.models.entities.recompensas.Recompensa;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.stream.Stream;

public class CanjeosRepository extends HibernateEntityManager<Canjeo, Long> {
  public Double findPuntosGastados(Colaborador colaborador) {
    // TODO: Cambiar el tipo de Recompensa.costoEnPuntos a @NonNull Long
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Double> query = cb.createQuery(Double.class);
    Root<Canjeo> root = query.from(Canjeo.class);

    query.select(cb.sum(root.get("recompensa").get("costoEnPuntos")))
        .where(cb.equal(root.get("colaborador"), colaborador));

    Double res = em.createQuery(query).getSingleResult();

    return res != null ? res : 0D;
  }

  public Double getPuntosDisponibles(Colaborador colaborador) {
    Double puntosTotales = CalculadoraDePuntos.calcular(colaborador);
    Double puntosGastados = findPuntosGastados(colaborador);

    return puntosTotales - puntosGastados;
  }

  public Stream<Canjeo> findAll(Recompensa recompensa) {
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Canjeo> query = cb.createQuery(Canjeo.class);
    Root<Canjeo> root = query.from(Canjeo.class);

    query.select(root).where(cb.equal(root.get("recompensa"), recompensa));

    return em.createQuery(query).getResultStream();
  }

  public long getStockDisponible(Recompensa recompensa) {
    return recompensa.getStockInicial() - findAll(recompensa).count();
  }

  public void insert(Canjeo canjeo) {
    Colaborador colaborador = canjeo.getColaborador();
    if (getPuntosDisponibles(colaborador) < canjeo.getRecompensa().getCostoEnPuntos()) {
      throw new RuntimeException("Colaborador no posee los puntos necesarios para la transacción");
    }
    if (getStockDisponible(canjeo.getRecompensa()) <= 0) {
      throw new RuntimeException("Recompensa no posee el stock necesario para la transacción");
    }

    super.insert(canjeo);
  }
}
