package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Email;
import ar.edu.utn.frba.dds.models.entities.contribucion.RubroRecompensa;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.recompensas.Canjeo;
import ar.edu.utn.frba.dds.models.entities.recompensas.Recompensa;
import ar.edu.utn.frba.dds.models.repositories.recompensas.CanjeosRepository;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import static org.mockito.Mockito.mock;

class CanjeosRepositoryTest {
  CanjeosRepository repositorio;
  Colaborador colaboradorDummy;
  Recompensa recompensaDummy;
  Canjeo canjeo;

  @BeforeEach
  void setUp() {
    repositorio = new CanjeosRepository();
    colaboradorDummy = new Colaborador(
        new Email("colaborador@example.com"),
        mock(Documento.class),
        "",
        "",
        LocalDate.now(),
        null
    );
    recompensaDummy = new Recompensa(
        "Recompensa dummy", RubroRecompensa.OTROS, 10, 1, null
    );
    canjeo = new Canjeo(colaboradorDummy, recompensaDummy, ZonedDateTime.now());
  }

  /*  TODO: Arreglar en la entrega de persistencia
  void registrarRedistribucionViandas(Colaborador colaborador, int cantidad) {
    // TODO: Arreglar cuando tengamos establecido el formato de registro de contribuciones
    new RedistribucionViandas(
        colaborador,
        Collections.nCopies(cantidad, null),
        null,
        null,
        null
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
  */
}