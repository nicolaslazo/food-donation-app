package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Email;
import ar.edu.utn.frba.dds.models.entities.contribucion.RedistribucionViandas;
import ar.edu.utn.frba.dds.models.entities.contribucion.RubroRecompensa;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import ar.edu.utn.frba.dds.models.entities.recompensas.Canjeo;
import ar.edu.utn.frba.dds.models.entities.recompensas.ExcepcionDeCanjeDePuntos;
import ar.edu.utn.frba.dds.models.entities.recompensas.Recompensa;
import ar.edu.utn.frba.dds.models.repositories.recompensas.CanjeosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CanjeosRepositoryTest {
  CanjeosRepository repositorio;
  Colaborador colaboradorDummy;
  Recompensa recompensaDummy;
  Canjeo canjeo;

  @BeforeEach
  void setUp() {
    repositorio = new CanjeosRepository();
    colaboradorDummy = new Colaborador(
        new Documento(TipoDocumento.DNI, 123),
        new Email("juan@example.com"),
        null,
        "",
        "",
        null);
    recompensaDummy = new Recompensa(
        "Recompensa dummy", RubroRecompensa.OTROS, 10, 1, null
    );
    canjeo = new Canjeo(colaboradorDummy, recompensaDummy, ZonedDateTime.now());
  }

  void registrarRedistribucionViandas(Colaborador colaborador, int cantidad) {
    // TODO: Arreglar cuando tengamos establecido el formato de registro de contribuciones
    new RedistribucionViandas(
        colaborador,
        ZonedDateTime.now(),
        null,
        null,
        null,
        Collections.nCopies(cantidad, null)
    );  // La instanciación se almacena sola en el colaborador (por ahora). 1 vianda = 1 punto
  }

  @Test
  void repositorioSeInstancia() {
    assertInstanceOf(CanjeosRepository.class, repositorio);
  }

  @Test
  void puedeRecolectarStockDisponible() {
    assertEquals(1, repositorio.getStockDisponible(recompensaDummy));
  }

  @Test
  void puedeRecolectarPuntosDisponiblesParaColaborador() {
    registrarRedistribucionViandas(colaboradorDummy, 10);

    assertEquals(10, repositorio.getPuntosDisponibles(colaboradorDummy));
  }

  @Test
  void canjeFallaSiColaboradorNoTienePuntos() {
    assertThrows(ExcepcionDeCanjeDePuntos.class, () -> repositorio.insert(canjeo));
  }

  @Test
  void insertRegistraUnCanjeDePuntos() {
    registrarRedistribucionViandas(colaboradorDummy, 10);

    assertDoesNotThrow(() -> repositorio.insert(canjeo));
    assertEquals(1, canjeo.getId());
    assertEquals(0, repositorio.getStockDisponible(recompensaDummy));
    assertEquals(0, repositorio.getPuntosDisponibles(colaboradorDummy));
  }

  @Test
  void canjesSePuedenLeer() throws ExcepcionDeCanjeDePuntos {
    registrarRedistribucionViandas(colaboradorDummy, 10);
    repositorio.insert(canjeo);

    assertEquals(canjeo, repositorio.get(1).orElse(null));
  }

  @Test
  void canjeFallaSiNoHayStock() throws ExcepcionDeCanjeDePuntos {
    registrarRedistribucionViandas(colaboradorDummy, 200);  // Tiene suficientes puntos para 2 transacciones
    repositorio.insert(canjeo);

    assertThrows(ExcepcionDeCanjeDePuntos.class, () -> repositorio.insert(canjeo));
  }

  @Test
  void leeTodosLosCanjes() throws ExcepcionDeCanjeDePuntos {
    Recompensa recompensaConStock = new Recompensa("recompensa con stock", RubroRecompensa.OTROS, 1, 3, null);
    Canjeo canjeoRecompensaConStock = new Canjeo(colaboradorDummy, recompensaConStock, ZonedDateTime.now());

    registrarRedistribucionViandas(colaboradorDummy, 3);
    while (repositorio.getPuntosDisponibles(colaboradorDummy) > 0) repositorio.insert(canjeoRecompensaConStock);

    assertEquals(3, repositorio.getTodos().size());
  }
}