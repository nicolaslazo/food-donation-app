package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.users.UsuariosRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;

class ViandasRepositoryTest {
  Colaborador colaborador;
  Heladera heladera;
  Vianda vianda, otraVianda;
  CoordenadasGeograficas obelisco;
  Usuario usuario;

  @BeforeEach
  void setUp() {
    obelisco = new CoordenadasGeograficas(-34.5611745, -58.4287506);
    colaborador = new Colaborador(new Documento(TipoDocumento.DNI, 1),
        "",
        "",
        LocalDate.now(),
        obelisco);
    heladera = new Heladera("",
        new CoordenadasGeograficas(-34d, -58d),
        colaborador,
        1,
        ZonedDateTime.now(),
        "");
    usuario = new Usuario(new Documento(TipoDocumento.DNI, 1),
        "",
        "",
        LocalDate.now(),
        null,
        new HashSet<>());
    vianda = new Vianda("",
        ZonedDateTime.now().plusWeeks(1),
        ZonedDateTime.now(),
        colaborador,
        1d,
        1);
    otraVianda = new Vianda("",
        ZonedDateTime.now().plusWeeks(1),
        ZonedDateTime.now(),
        colaborador,
        1d,
        1);
    new ColaboradorRepository().insert(colaborador);
    new UsuariosRepository().insert(usuario);
    new HeladerasRepository().insert(heladera);
    vianda.setHeladera(heladera);
  }

  @AfterEach
  void tearDown() {
    new HibernatePersistenceReset().execute();
  }
}