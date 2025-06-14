package ar.edu.utn.frba.dds.controllers.contribucion;

import ar.edu.utn.frba.dds.controllers.ViandaController;
import ar.edu.utn.frba.dds.controllers.heladera.HeladeraController;
import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.HibernatePersistenceReset;
import ar.edu.utn.frba.dds.models.repositories.ViandasRepository;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.users.UsuariosRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DonacionViandaControllerTest {
  Colaborador colaborador;
  Usuario usuario;
  Heladera heladera;
  Vianda vianda, otraVianda;
  CoordenadasGeograficas obelisco;
  ViandaController viandaController;
  ViandasRepository repository;

  @BeforeEach
  public void setUp() {
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
    viandaController = new ViandaController();
    repository = new ViandasRepository();
  }

  @AfterEach
  public void tearDown() {
    new HibernatePersistenceReset().execute();
  }

  @Test
  void testInsertDeCollectionFallaSiViandasTienenHeladerasDistintas() {
    Collection<Vianda> viandas = List.of(vianda, otraVianda);
    assertNull(otraVianda.getHeladera());
    assertThrows(RuntimeException.class,
            () -> {
              viandaController.assertViandasSonDeLaMismaHeladera(List.of(vianda, otraVianda));
              repository.insertAll(viandas);
            }
    );
    assertEquals(0, repository.findAll(heladera).count());
  }

  @Test
  void testInsertDeCollectionFallaSiHayDemasiadasViandas() {
    otraVianda.setHeladera(heladera);
    Collection<Vianda> viandas = List.of(vianda, otraVianda);
    assertThrows(RuntimeException.class,
            () -> {
              new HeladeraController().assertHeladeraTieneSuficienteEspacio(heladera, viandas.size());
              repository.insertAll(viandas);
            });
    assertEquals(0, repository.findAll().count());
  }
}
