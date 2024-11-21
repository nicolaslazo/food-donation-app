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

    Vianda vianda6 = new Vianda(
        "Tarta de espinaca y queso",
        ZonedDateTime.now().plusDays(2),
        ZonedDateTime.now(),
        colaborador,
        300.0,
        350
    );

    Vianda vianda7 = new Vianda(
        "Milanesa de soja con puré",
        ZonedDateTime.now().plusDays(4),
        ZonedDateTime.now(),
        colaborador,
        450.0,
        500
    );

    Vianda vianda8 = new Vianda(
        "Risotto de hongos",
        ZonedDateTime.now().plusDays(3),
        ZonedDateTime.now(),
        colaborador,
        400.0,
        600
    );

    Vianda vianda9 = new Vianda(
        "Empanadas de carne",
        ZonedDateTime.now().plusDays(1),
        ZonedDateTime.now(),
        colaborador,
        300.0,
        700
    );

    Vianda vianda10 = new Vianda(
        "Pizza margarita",
        ZonedDateTime.now().plusDays(5),
        ZonedDateTime.now(),
        colaborador,
        250.0,
        800
    );

    Vianda vianda11 = new Vianda(
        "Hamburguesa con papas fritas",
        ZonedDateTime.now().plusDays(2),
        ZonedDateTime.now(),
        colaborador,
        500.0,
        900
    );

    Vianda vianda12 = new Vianda(
        "Canelones de ricota",
        ZonedDateTime.now().plusDays(6),
        ZonedDateTime.now(),
        colaborador,
        350.0,
        450
    );

    Vianda vianda13 = new Vianda(
        "Guiso de lentejas",
        ZonedDateTime.now().plusDays(4),
        ZonedDateTime.now(),
        colaborador,
        400.0,
        550
    );

    Vianda vianda14 = new Vianda(
        "Tacos de pollo",
        ZonedDateTime.now().plusDays(3),
        ZonedDateTime.now(),
        colaborador,
        450.0,
        650
    );

    Vianda vianda15 = new Vianda(
        "Wrap de vegetales",
        ZonedDateTime.now().plusDays(1),
        ZonedDateTime.now(),
        colaborador,
        300.0,
        300
    );

    Vianda viandaASerRobada = new Vianda(
        "Pollo al horno robado con papas robadas",
        ZonedDateTime.now().plusDays(3),
        ZonedDateTime.now(),
        colaborador,
        450.0,
        600
    );

    // Distribuir viandas en heladeras
    List<Heladera> heladeras = new HeladerasRepository().findAll().toList();

    Set<Vianda> tandaDeViandas1 = Set.of(vianda1, vianda2, vianda3, vianda4, vianda5);
    for (Vianda vianda : tandaDeViandas1) vianda.setHeladera(heladeras.get(0));

    Set<Vianda> tandaDeViandas2 = Set.of(vianda6, vianda7, vianda8, vianda9, vianda10);
    for (Vianda vianda : tandaDeViandas2) vianda.setHeladera(heladeras.get(1));

    Set<Vianda> tandaDeViandas3 = Set.of(vianda11, vianda12, vianda13, vianda14, vianda15);
    for (Vianda vianda : tandaDeViandas3) vianda.setHeladera(heladeras.get(2));

    // Configurar caso de fraude
    viandaASerRobada.setHeladera(heladeras.get(0));

    // Insertar todas las viandas
    new ViandasRepository().insertAll(tandaDeViandas1);
    new ViandasRepository().insertAll(tandaDeViandas2);
    new ViandasRepository().insertAll(tandaDeViandas3);
    new ViandasRepository().insert(viandaASerRobada);

    // Crear redistribución fraudulenta
    RedistribucionViandas redistribucionIncompleta = new RedistribucionViandas(
        SeederUsuarios.colaboradorFraudulento,
        Set.of(viandaASerRobada),
        heladeras.get(1),
        heladeras.get(0),
        MotivoDeDistribucion.FALTA_ESPACIO
    );

    new RedistribucionViandasRepository().insert(redistribucionIncompleta);

    // Crear solicitud vencida
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