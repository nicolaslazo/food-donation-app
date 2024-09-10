package ar.edu.utn.frba.dds.models.repositories.heladera;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.heladera.SolicitudAperturaPorConsumicion;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.repositories.HibernatePersistenceReset;
import ar.edu.utn.frba.dds.models.repositories.ViandasRepository;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.documentacion.TarjetasRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.UUID;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public class SolicitudAperturaPorConsumicionRepositoryTest {

  CoordenadasGeograficas obelisco = new CoordenadasGeograficas(-34.5611745, -58.4287506);

  final SolicitudAperturaPorConsumicionRepository repositorio = new SolicitudAperturaPorConsumicionRepository();
  Colaborador colaborador = new Colaborador(new Documento(TipoDocumento.DNI, 1),
      "",
      "",
      LocalDate.now(),
      new CoordenadasGeograficas(-34., -58.));

  Heladera heladera = new Heladera("Una heladera",
      obelisco,
      colaborador,
      50,
      ZonedDateTime.now().minusMonths(5),
      ""
  );
  Vianda vianda = new Vianda("",
      ZonedDateTime.now().plusWeeks(1),
      ZonedDateTime.now(),
      colaborador,
      1d,
      1);

  Tarjeta tarjeta = new Tarjeta(UUID.randomUUID());

  SolicitudAperturaPorConsumicion solicitud =
      new SolicitudAperturaPorConsumicion(tarjeta, vianda, heladera, ZonedDateTime.now().minusMonths(2));
  SolicitudAperturaPorConsumicion otraSolicitud =
      new SolicitudAperturaPorConsumicion(tarjeta, vianda, heladera, ZonedDateTime.now().minusMonths(1));

  @AfterEach
   void tearDown() {
    new HibernatePersistenceReset().execute();
  }

  @BeforeEach
  void setUp() {
    new ColaboradorRepository().insert(colaborador);
    new HeladerasRepository().insert(heladera);
    vianda.setHeladera(heladera);
    new ViandasRepository().insert(vianda);
    new TarjetasRepository().insert(tarjeta);
    repositorio.insert(solicitud);
  }
  @Test
  void testGetPorId() {

    repositorio.insert(solicitud);
    repositorio.insert(otraSolicitud);

    assertEquals(otraSolicitud, repositorio.findById(otraSolicitud.getId()).orElseThrow());
  }

  @Test
  void testInsertarSinFallar() {
    assertDoesNotThrow(()-> new SolicitudAperturaPorConsumicionRepository().insert(solicitud));
  }

  @Test
  void testEliminarTodas() {
    repositorio.insert(solicitud);
    repositorio.insert(otraSolicitud);
    repositorio.deleteAll();
    assertEquals(0, repositorio.findAll().count());
  }

  @Test
  void getTodosRetornaTodosLosContenidos() {

    repositorio.insert(solicitud);
    repositorio.insert(otraSolicitud);

    assertEquals(2, repositorio.findAll().count());
  }
}

