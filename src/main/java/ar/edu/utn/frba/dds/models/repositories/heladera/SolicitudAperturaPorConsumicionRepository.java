package ar.edu.utn.frba.dds.models.repositories.heladera;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudAperturaPorConsumicion;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

public class SolicitudAperturaPorConsumicionRepository
    extends HibernateEntityManager<SolicitudAperturaPorConsumicion, Long> {
  public static void main(String[] args) {
    /* Demo. Carga todos los elementos hijos a persistencia para análisis */
    SolicitudAperturaPorConsumicionRepository repository = new SolicitudAperturaPorConsumicionRepository();

    String[] nombres = {"Juan", "Maria", "Carlos", "Ana", "Pedro", "Laura", "Diego", "Sofia", "Luis", "Elena"};
    String[] apellidos = {"Perez", "Gomez", "Rodriguez", "Lopez", "Martinez", "Garcia", "Fernandez", "Sanchez", "Romero", "Torres"};
    String[] comidas = {"guiso", "milanesa", "pasta", "ensalada", "sopa", "pizza", "hamburguesa", "pollo", "pescado", "arroz"};

    for (int i = 0; i < 10; i++) {
      Tarjeta tarjeta = new Tarjeta(UUID.randomUUID());
      Documento documento = new Documento(TipoDocumento.DNI, 40000000 + i);
      Colaborador colaborador = new Colaborador(documento,
          nombres[i],
          apellidos[i],
          LocalDate.now().minusYears(25 + i),
          null);
      Vianda vianda = new Vianda(comidas[i],
          ZonedDateTime.now().plusWeeks(1),
          ZonedDateTime.now(),
          colaborador,
          300d + (i * 10),
          700 + (i * 50));
      SolicitudAperturaPorConsumicion solicitud =
          new SolicitudAperturaPorConsumicion(tarjeta, vianda, ZonedDateTime.now().minusDays(i));

      repository.insert(solicitud);
      System.out.println("Solicitud insertada para " + nombres[i] + " " + apellidos[i]);
    }

  }
}
