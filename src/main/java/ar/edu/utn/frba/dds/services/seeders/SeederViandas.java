package ar.edu.utn.frba.dds.services.seeders;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.MotivoDeDistribucion;
import ar.edu.utn.frba.dds.models.entities.contribucion.RedistribucionViandas;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudAperturaPorContribucion;
import ar.edu.utn.frba.dds.models.repositories.ViandasRepository;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.contribucion.RedistribucionViandasRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.SolicitudAperturaPorContribucionRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

public class SeederViandas {
  public static void execute() throws Exception {
    Colaborador colaborador = new ColaboradorRepository().findAll().findFirst().get();

    Vianda vianda1 = new Vianda(
        "Pollo al horno con papas",
        ZonedDateTime.now().plusDays(3),
        ZonedDateTime.now(),
        colaborador,
        450.0,
        600
    );

    Vianda vianda2 = new Vianda(
        "Ensalada César",
        ZonedDateTime.now().plusDays(2),
        ZonedDateTime.now(),
        colaborador,
        300.0,
        350
    );

    Vianda vianda3 = new Vianda(
        "Pasta con salsa boloñesa",
        ZonedDateTime.now().plusDays(4),
        ZonedDateTime.now(),
        colaborador,
        400.0,
        550
    );

    Vianda vianda4 = new Vianda(
        "Sopa de verduras",
        ZonedDateTime.now().plusDays(5),
        ZonedDateTime.now(),
        colaborador,
        350.0,
        200
    );

    Vianda vianda5 = new Vianda(
        "Sanguche de jamón y queso",
        ZonedDateTime.now().plusDays(1),
        ZonedDateTime.now(),
        colaborador,
        250.0,
        400
    );

    Vianda viandaASerRobada = new Vianda(
        "Pollo al horno robado con papas robadas",
        ZonedDateTime.now().plusDays(3),
        ZonedDateTime.now(),
        colaborador,
        450.0,
        600
    );

    Set<Vianda> todasLasViandas = Set.of(vianda1, vianda2, vianda3, vianda4, vianda5, viandaASerRobada);
    List<Heladera> heladeras = new HeladerasRepository().findAll().toList();
    Heladera unaHeladera = heladeras.get(0);
    Heladera otraHeladera = heladeras.get(1);

    for (Vianda vianda : todasLasViandas) vianda.setHeladera(unaHeladera);
    new ViandasRepository().insertAll(todasLasViandas);

    RedistribucionViandas redistribucionIncompleta = new RedistribucionViandas(
        SeederUsuarios.colaboradorFraudulento,
        Set.of(viandaASerRobada),
        otraHeladera,
        unaHeladera,
        MotivoDeDistribucion.FALTA_ESPACIO
    );

    new RedistribucionViandasRepository().insert(redistribucionIncompleta);

    ZonedDateTime timestampCreacionRedistribucion = ZonedDateTime.now().minusDays(2);
    ZonedDateTime timestampInicioRedistribucion = timestampCreacionRedistribucion.plusMinutes(5);

    SolicitudAperturaPorContribucion solicitudVencida =
        new SolicitudAperturaPorContribucion(
            SeederUsuarios.tarjetaFraudulento,
            redistribucionIncompleta,
            timestampInicioRedistribucion.minusMinutes(5));
    solicitudVencida.setFechaAperturaEnOrigen(timestampInicioRedistribucion);

    new SolicitudAperturaPorContribucionRepository().insert(solicitudVencida);
  }
}