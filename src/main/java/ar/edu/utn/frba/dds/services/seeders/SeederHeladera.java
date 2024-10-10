package ar.edu.utn.frba.dds.services.seeders;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;

import javax.annotation.PostConstruct;
import java.time.ZonedDateTime;

public class SeederHeladera {
  Colaborador unColaborador = new ColaboradorRepository().findAll().findFirst().get();
  HeladerasRepository heladerasRepository = new HeladerasRepository();

  @PostConstruct
  public void seederHeladera() {
    Heladera heladerauno = new Heladera(
        "Primera Heladera",
        new CoordenadasGeograficas(-34.6025246, -58.3843585),
        unColaborador,
        22,
        ZonedDateTime.now(),
        "San Nicolas"
    );
    Heladera heladerados = new Heladera(
        "Segunda Heladera",
        new CoordenadasGeograficas(-34.6024605, -58.3812774),
        unColaborador,
        22,
        ZonedDateTime.now(),
        "San Nicolas"
    );
    Heladera heladeratre = new Heladera(
        "Tercera Heladera",
        new CoordenadasGeograficas(-34.6047476, -58.3795161),
        unColaborador,
        22,
        ZonedDateTime.now(),
        "San Nicolas"
    );
    Heladera heladeraMedrano = new Heladera(
        "heladera de medrano",
        new CoordenadasGeograficas(-34.598607968791924, -58.420107873800205),
        unColaborador,
        22,
        ZonedDateTime.now(),
        "Almagro"
    );
    heladerasRepository.insert(heladerauno);
    heladerasRepository.insert(heladerados);
    heladerasRepository.insert(heladeratre);
    heladerasRepository.insert(heladeraMedrano);
  }
}
