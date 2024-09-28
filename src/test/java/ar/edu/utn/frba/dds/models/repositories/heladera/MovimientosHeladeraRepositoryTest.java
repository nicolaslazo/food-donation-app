package ar.edu.utn.frba.dds.models.repositories.heladera;


import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.heladera.EventoMovimiento;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.repositories.HibernatePersistenceReset;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.time.LocalDate;
import java.time.ZonedDateTime;

class MovimientosHeladeraRepositoryTest {
  MovimientosHeladeraRepository repositorio = new MovimientosHeladeraRepository();
  CoordenadasGeograficas obelisco = new CoordenadasGeograficas(-34.5611745, -58.4287506);

  Colaborador colaborador = new Colaborador(
      new Documento(TipoDocumento.DNI, 1),
      "",
      "",
      LocalDate.now(),
      new CoordenadasGeograficas(-30d, -50d));
  Heladera heladera = new Heladera("Una heladera",
      obelisco,
      colaborador,
      50,
      ZonedDateTime.now().minusMonths(5),
      "Barrio"
  );

  EventoMovimiento eventoMovimiento = new EventoMovimiento(heladera, ZonedDateTime.now());
  
  @BeforeEach
  void setUp() {
    new ColaboradorRepository().insert(colaborador);
    new HeladerasRepository().insert(heladera);
    repositorio.insert(eventoMovimiento);
  }

  @AfterEach
  void tearDown() {
    new HibernatePersistenceReset().execute();
  }
  @Test
  void repositorioSeInstancia() {
    assertInstanceOf(MovimientosHeladeraRepository.class, repositorio);
  }

  @Test
  void testInsert() {
    assertNotNull(eventoMovimiento.getId());
  }

}
