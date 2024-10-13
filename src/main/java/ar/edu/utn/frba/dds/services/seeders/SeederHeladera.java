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
    Heladera heladeraUno = new Heladera(
        "Primera Heladera",
        new CoordenadasGeograficas(-34.6025246, -58.3843585),
        unColaborador,
        22,
        ZonedDateTime.now(),
        "San Nicolas"
    );
    Heladera heladeraDos = new Heladera(
        "Segunda Heladera",
        new CoordenadasGeograficas(-34.6024605, -58.3812774),
        unColaborador,
        22,
        ZonedDateTime.now(),
        "San Nicolas"
    );
    Heladera heladeraTres = new Heladera(
        "Tercera Heladera",
        new CoordenadasGeograficas(-34.6047476, -58.3795161),
        unColaborador,
        22,
        ZonedDateTime.now(),
        "San Nicolas"
    );
    Heladera heladeraMedrano = new Heladera(
        "Heladera Medrano",
        new CoordenadasGeograficas(-34.598607968791924, -58.420107873800205),
        unColaborador,
        22,
        ZonedDateTime.now(),
        "Almagro"
    );
    heladerasRepository.insert(heladeraUno);
    heladerasRepository.insert(heladeraDos);
    heladerasRepository.insert(heladeraTres);
    heladerasRepository.insert(heladeraMedrano);
  }
}
