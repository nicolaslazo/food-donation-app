package ar.edu.utn.frba.dds.models.repositories.heladera;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudAperturaPorConsumicion;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudInvalidaException;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.entities.users.PermisoDenegadoException;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

public class SolicitudAperturaPorConsumicionRepository
    extends HibernateEntityManager<SolicitudAperturaPorConsumicion, Long> {
  public static void main(String[] args) throws PermisoDenegadoException {
    /* Demo. Carga todos los elementos hijos a persistencia para análisis */
    SolicitudAperturaPorConsumicionRepository repository = new SolicitudAperturaPorConsumicionRepository();

    String[] nombres = {"Juan", "Maria", "Carlos", "Ana", "Pedro", "Laura", "Diego", "Sofia", "Luis", "Elena"};
    String[] apellidos = {"Perez", "Gomez", "Rodriguez", "Lopez", "Martinez", "Garcia", "Fernandez", "Sanchez", "Romero", "Torres"};
    String[] comidas = {"guiso", "milanesa", "pasta", "ensalada", "sopa", "pizza", "hamburguesa", "pollo", "pescado", "arroz"};
    String[] barrios = {"almagro", "almagro", "almagro", "microcentro", "microcentro", "microcentro", "microcentro", "microcentro", "microcentro", "microcentro"};

    for (int i = 0; i < 10; i++) {
      Documento documento = new Documento(TipoDocumento.DNI, 40000000 + i);
      Colaborador colaborador = new Colaborador(documento,
          nombres[i],
          apellidos[i],
          LocalDate.now().minusYears(25 + i),
          null);
      Tarjeta tarjeta = new Tarjeta(UUID.randomUUID());
      tarjeta.setEnAlta(colaborador.getUsuario(), colaborador, ZonedDateTime.now());
      Vianda vianda = new Vianda(comidas[i],
          ZonedDateTime.now().plusWeeks(1),
          ZonedDateTime.now(),
          colaborador,
          300d + (i * 10),
          700 + (i * 50));
      Heladera heladera = new Heladera("Heladera " + i,
          new CoordenadasGeograficas(-34d + i, -58d + i),
          colaborador,
          10,
          ZonedDateTime.now(),
          barrios[i]);
      vianda.setHeladera(heladera);
      SolicitudAperturaPorConsumicion solicitud =
          new SolicitudAperturaPorConsumicion(tarjeta, vianda, ZonedDateTime.now().minusDays(i));

      repository.insert(solicitud);
      System.out.println("Solicitud insertada para " + nombres[i] + " " + apellidos[i]);
    }
  }

  public Optional<SolicitudAperturaPorConsumicion> getSolicitudVigenteAlMomento(Long id, ZonedDateTime momento) {
    EntityManager em = entityManager();
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<SolicitudAperturaPorConsumicion> query = cb.createQuery(SolicitudAperturaPorConsumicion.class);
    Root<SolicitudAperturaPorConsumicion> root = query.from(SolicitudAperturaPorConsumicion.class);
    query.select(root).where(cb.equal(root.get("id"), id));
    return em.createQuery(query)
        .getResultStream()
        .filter(solicitud -> solicitud.isVigenteAlMomento(momento))
        .findFirst();
  }

  public void updateFechaUsada(Long id, ZonedDateTime fechaUsada)
      throws Exception {
    SolicitudAperturaPorConsumicion solicitud =
        getSolicitudVigenteAlMomento(id, fechaUsada)
            .orElseThrow(() -> new SolicitudInvalidaException("No existe solicitud vigente con id %d".formatted(id)));

    solicitud.setFechaUsada(fechaUsada);

    update(solicitud);
  }

  public Long findCantidadUsadasHoy(Tarjeta tarjeta) {
    var em = entityManager();
    var cb = em.getCriteriaBuilder();
    var query = cb.createQuery(Long.class);
    var root = query.from(SolicitudAperturaPorConsumicion.class);

    // Obtener el inicio y fin del día actual en la zona horaria del sistema
    ZonedDateTime inicioDia = ZonedDateTime.now().toLocalDate().atStartOfDay(ZonedDateTime.now().getZone());
    ZonedDateTime finDia = inicioDia.plusDays(1);

    query.select(cb.count(root))
        .where(cb.and(
            cb.equal(root.get("tarjeta"), tarjeta),
            cb.isNotNull(root.get("fechaUsada")),
            cb.greaterThanOrEqualTo(root.get("fechaUsada"), inicioDia),
            cb.lessThan(root.get("fechaUsada"), finDia)
        ));

    return em.createQuery(query).getSingleResult();
  }
}
