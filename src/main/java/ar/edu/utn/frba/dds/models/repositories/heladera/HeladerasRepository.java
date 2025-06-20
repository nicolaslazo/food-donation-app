package ar.edu.utn.frba.dds.models.repositories.heladera;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;
import ar.edu.utn.frba.dds.models.repositories.ViandasRepository;
import lombok.NonNull;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.time.ZonedDateTime;
import java.util.stream.Stream;

public class HeladerasRepository extends HibernateEntityManager<Heladera, Long> {
  public Stream<Heladera> findConTemperaturaDesactualizada(int limiteEnMinutos) {
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Heladera> query = cb.createQuery(Heladera.class);
    Root<Heladera> root = query.from(Heladera.class);

    ZonedDateTime limite = ZonedDateTime.now().minusMinutes(limiteEnMinutos);

    query.select(root).where(cb.lessThan(root.get("momentoUltimaTempRegistrada"), limite));

    return em.createQuery(query).getResultStream();
  }

  public Stream<Heladera> findAll(@NonNull Colaborador encargado) {
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Heladera> query = cb.createQuery(Heladera.class);
    Root<Heladera> root = query.from(Heladera.class);

    query.select(root).where(cb.equal(root.get("encargado"), encargado));

    return em.createQuery(query).getResultStream();
  }

  public Stream<Heladera> findHeladerasConViandas() {
    return entityManager()
            .createQuery("SELECT DISTINCT h FROM Heladera h JOIN Vianda v ON v.heladera = h", Heladera.class)
            .getResultStream();
  }


  public void updateTiempoHeladera(Long id, Double temperaturaNueva) {
    Heladera heladera = findById(id).orElseThrow();
    heladera.setUltimaTempRegistradaCelsius(temperaturaNueva);

    withTransaction(() -> merge(heladera));
  }

  public int getMesesActivosCumulativos(Colaborador colaborador) {
    // TODO: Actualizar en base a la fecha del último incidente resuelto
    return findAll(colaborador).mapToInt(Heladera::mesesActiva).sum();
  }

  /* Este método concierne a la cantidad de viandas ahora mismo depositadas en la heladera, independientemente de las
   * Solicitudes de apertura. Para saber el espacio disponible, reservando los espacios de las solicitudes de apertura
   */
  public long getCantidadViandasDepositadas(Heladera heladera) {
    return new ViandasRepository().findAll(heladera).count();
  }

  public long getCapacidadDisponible(Heladera heladera) {
    SolicitudAperturaPorContribucionRepository repositorio = new SolicitudAperturaPorContribucionRepository();
    final int viandasEnContribucionesVigentes =
        repositorio.getCantidadViandasPendientes(heladera);

    return heladera.getCapacidadEnViandas() - getCantidadViandasDepositadas(heladera) - viandasEnContribucionesVigentes;
  }

  public long getCantidadViandasDisponiblesARetirar(Heladera heladera) {
    EntityManager em = entityManager();

    String jpql = "SELECT COUNT(v) FROM Vianda v " +
            "LEFT JOIN SolicitudAperturaPorConsumicion s ON s.vianda = v " +
            "WHERE v.heladera = :heladera AND (s IS NULL OR s.fechaVencimiento < :now)";

    return em.createQuery(jpql, Long.class)
            .setParameter("heladera", heladera)
            .setParameter("now", ZonedDateTime.now())
            .getSingleResult();
  }
}
