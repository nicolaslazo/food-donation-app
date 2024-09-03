package ar.edu.utn.frba.dds.models.repositories.heladera;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;
import ar.edu.utn.frba.dds.models.repositories.ViandasRepository;
import lombok.NonNull;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.stream.Stream;

public class HeladerasRepository extends HibernateEntityManager<Heladera, Long> {
  public Optional<Heladera> find(@NonNull CoordenadasGeograficas ubicacion) {
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Heladera> query = cb.createQuery(Heladera.class);
    Root<Heladera> root = query.from(Heladera.class);

    query.select(root).where(cb.equal(root.get("ubicacion"), ubicacion));

    return em.createQuery(query).getResultList().stream().findFirst();
  }

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

    query.select(root).where(cb.equal(root.get("colaborador"), encargado));

    return em.createQuery(query).getResultStream();
  }

  // TODO: arreglar el tipado acá antes de mergear. Tomamos una heladera o un double de temperatura?
  public void updateTiempoHeladera(Long id, Double temperaturaNueva) {
//    Heladera heladera = findById(id).orElseThrow();
//    heladera.setUltimaTempRegistradaCelsius(temperaturaNueva);
//
//    withTransaction(() -> merge(heladera));
  }

  public int getMesesActivosCumulativos(Colaborador colaborador) {
    // TODO: Actualizar en base a la fecha del último incidente resuelto
    return findAll(colaborador).mapToInt(Heladera::mesesActiva).sum();
  }

  /* Este método concierne a la cantidad de viandas ahora mismo depositadas en la heladera, independientemente de las
   * Solicitudes de apertura. Para saber el espacio disponible, reservando los espacios de las solicitudes de apertura
   */
  public int getCantidadViandasDepositadas(Heladera heladera) {
    return ViandasRepository.getInstancia().getAlmacenadas(heladera).size();
  }

  public int getCapacidadDisponible(Heladera heladera) {
    final int viandasEnContribucionesVigentes =
        SolicitudAperturaPorContribucionRepository.getInstancia().getCantidadViandasPendientes(heladera);

    return heladera.getCapacidadEnViandas() - getCantidadViandasDepositadas(heladera) - viandasEnContribucionesVigentes;
  }
}

