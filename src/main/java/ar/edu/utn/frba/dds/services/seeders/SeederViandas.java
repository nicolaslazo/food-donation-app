package ar.edu.utn.frba.dds.services.seeders;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.ViandasRepository;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;

import java.time.ZonedDateTime;
import java.util.Set;

public class SeederViandas {
  public static void execute() {
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

    Set<Vianda> todasLasViandas = Set.of(vianda1, vianda2, vianda3, vianda4, vianda5);
    Heladera heladera = new HeladerasRepository().findAll().findFirst().get();

    for (Vianda vianda : todasLasViandas) vianda.setHeladera(heladera);

    new ViandasRepository().insertAll(todasLasViandas);
  }
}