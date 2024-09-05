package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.Dinero;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.ubicacion.CoordenadasGeograficas;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class DineroRepositoryTest {
  Colaborador colaborador = new Colaborador(
          new Documento(TipoDocumento.DNI, 1),
          "",
          "",
          LocalDate.now(),
          new CoordenadasGeograficas(-30d, -50d));

  Dinero donacion = new Dinero(colaborador, 1000f, null);

  @BeforeEach
  void setUp() {
    new DineroRepository().insert(donacion);
  }

  @AfterEach
  void tearDown(){
    new DineroRepository().deleteAll();
    new ColaboradorRepository().deleteAll();
  }

  @Test
  void testGetPorId() {
    Optional<Dinero> encontrada = new DineroRepository().findById(donacion.getId());

    assertTrue(encontrada.isPresent());
    assertEquals(donacion.getId(), encontrada.get().getId());
  }

  @Test
  void testObtenerTotalPorColaborador() {
    Dinero otraDonacion = new Dinero(colaborador, 500f, null);
    new DineroRepository().insert(otraDonacion);

    double total = new DineroRepository().getTotal(colaborador);

    assertEquals(1500.0f, total);
  }
}
