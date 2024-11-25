package ar.edu.utn.frba.dds.services.seeders;

import ar.edu.utn.frba.dds.models.entities.Tecnico;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.CuidadoHeladera;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.Incidente;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.TipoIncidente;
import ar.edu.utn.frba.dds.models.entities.heladera.incidente.VisitaTecnica;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.repositories.TecnicoRepository;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.contribucion.CuidadoHeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.incidente.IncidenteRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.incidente.VisitasTecnicasRepository;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.List;

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
        ZonedDateTime.now().minusMonths(6),
        "San Nicolas"
    );
    Heladera heladeraDos = new Heladera(
        "Segunda Heladera",
        new CoordenadasGeograficas(-34.6024605, -58.3812774),
        unColaborador,
        22,
        ZonedDateTime.now().minusMonths(6),
        "San Nicolas"
    );
    Heladera heladeraTres = new Heladera(
        "Tercera Heladera",
        new CoordenadasGeograficas(-34.6047476, -58.3795161),
        unColaborador,
        22,
        ZonedDateTime.now().minusMonths(6),
        "San Nicolas"
    );
    Heladera heladeraMedrano = new Heladera(
        "Heladera Medrano",
        new CoordenadasGeograficas(-34.598607968791924, -58.420107873800205),
        unColaborador,
        22,
        ZonedDateTime.now().minusMonths(6),
        "Almagro"
    );
    Heladera heladeraCasiLlena = new Heladera(
        "Heladera Casi Llena",
        new CoordenadasGeograficas(-34.618515, -58.375410),
        unColaborador,
        1,
        ZonedDateTime.now(),
        "San Telmo"
    );
    Heladera heladeraDefectuosa = new Heladera(
        "Heladera Defectuosa",
        new CoordenadasGeograficas(-34.616728, -58.381860),
        unColaborador,
        22,
        ZonedDateTime.now().minusHours(2),
        "UADE"
    );

    // --- Incidentes ---

    Incidente incidente = new Incidente(
        heladeraDefectuosa,
        TipoIncidente.FALLA_REPORTADA_POR_COLABORADOR,
        ZonedDateTime.now().minusHours(1),
        unColaborador,
        "Hace ruidos raros en algunos momentos.",
        null
    );

    Tecnico tecnico = new TecnicoRepository().findAll().findFirst().get();

    VisitaTecnica visitaTecnica = new VisitaTecnica(
        tecnico,
        incidente,
        ZonedDateTime.now(),
        false,
        "No tengo los repuestos necesarios.",
        null
    );

    // --- Cuidado Heladera ---

    CuidadoHeladerasRepository cuidadoHeladerasRepository = new CuidadoHeladerasRepository();

    CuidadoHeladera cuidadoHeladeraUno = new CuidadoHeladera(unColaborador, heladeraUno);

    CuidadoHeladera cuidadoHeladeraDos = new CuidadoHeladera(unColaborador, heladeraDos);

    CuidadoHeladera cuidadoHeladeraTres = new CuidadoHeladera(unColaborador, heladeraTres);

    CuidadoHeladera cuidadoHeladeraMedrano = new CuidadoHeladera(unColaborador, heladeraMedrano);

    CuidadoHeladera cuidadoHeladeraCasiLlena = new CuidadoHeladera(unColaborador, heladeraCasiLlena);

    CuidadoHeladera cuidadoHeladeraDefectuosa = new CuidadoHeladera(unColaborador, heladeraDefectuosa);

    heladerasRepository.insertAll(List.of(heladeraUno, heladeraDos, heladeraTres,
            heladeraMedrano, heladeraCasiLlena, heladeraDefectuosa));

    cuidadoHeladerasRepository.insertAll(List.of(cuidadoHeladeraUno, cuidadoHeladeraDos, cuidadoHeladeraTres,
            cuidadoHeladeraMedrano, cuidadoHeladeraCasiLlena, cuidadoHeladeraDefectuosa));

    new IncidenteRepository().insert(incidente);

    new VisitasTecnicasRepository().insert(visitaTecnica);
  }
}
