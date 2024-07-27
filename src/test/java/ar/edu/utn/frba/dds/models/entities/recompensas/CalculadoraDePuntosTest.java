package ar.edu.utn.frba.dds.models.entities.recompensas;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.repositories.contribucion.CuidadoHeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.contribucion.DineroRepository;
import ar.edu.utn.frba.dds.models.repositories.contribucion.DonacionViandasRepository;
import ar.edu.utn.frba.dds.models.repositories.contribucion.EntregaTarjetasRepository;
import ar.edu.utn.frba.dds.models.repositories.contribucion.RedistribucionViandasRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculadoraDePuntosTest {
  @Mock
  Colaborador colaboradorMock;
  @Mock
  DineroRepository repositorioDineroMock;
  @Mock
  RedistribucionViandasRepository repositorioViandasRedistribuidasMock;
  @Mock
  DonacionViandasRepository repositorioViandasDonadasMock;
  @Mock
  EntregaTarjetasRepository repositorioEntregaTarjetasMock;
  @Mock
  CuidadoHeladerasRepository repositorioCuidadoHeladerasMock;

  @AfterAll
  static void afterAll() throws NoSuchFieldException, IllegalAccessException {
    Field campoInstancia;

    campoInstancia = DineroRepository.class.getDeclaredField("instancia");
    campoInstancia.setAccessible(true);
    campoInstancia.set(null, null);

    campoInstancia = RedistribucionViandasRepository.class.getDeclaredField("instancia");
    campoInstancia.setAccessible(true);
    campoInstancia.set(null, null);

    campoInstancia = DonacionViandasRepository.class.getDeclaredField("instancia");
    campoInstancia.setAccessible(true);
    campoInstancia.set(null, null);

    campoInstancia = EntregaTarjetasRepository.class.getDeclaredField("instancia");
    campoInstancia.setAccessible(true);
    campoInstancia.set(null, null);

    campoInstancia = CuidadoHeladerasRepository.class.getDeclaredField("instancia");
    campoInstancia.setAccessible(true);
    campoInstancia.set(null, null);
  }

  @BeforeEach
  void setUp() throws NoSuchFieldException, IllegalAccessException {
    // TODO: Hay alguna manera más elegante de hacer esto?
    Field campoInstancia;

    campoInstancia = DineroRepository.class.getDeclaredField("instancia");
    campoInstancia.setAccessible(true);
    campoInstancia.set(null, repositorioDineroMock);

    campoInstancia = RedistribucionViandasRepository.class.getDeclaredField("instancia");
    campoInstancia.setAccessible(true);
    campoInstancia.set(null, repositorioViandasRedistribuidasMock);

    campoInstancia = DonacionViandasRepository.class.getDeclaredField("instancia");
    campoInstancia.setAccessible(true);
    campoInstancia.set(null, repositorioViandasDonadasMock);

    campoInstancia = EntregaTarjetasRepository.class.getDeclaredField("instancia");
    campoInstancia.setAccessible(true);
    campoInstancia.set(null, repositorioEntregaTarjetasMock);

    campoInstancia = CuidadoHeladerasRepository.class.getDeclaredField("instancia");
    campoInstancia.setAccessible(true);
    campoInstancia.set(null, repositorioCuidadoHeladerasMock);
  }

  @Test
  public void testCalculaPuntaje() {
    when(repositorioDineroMock.getTotal(colaboradorMock)).thenReturn(1.0);  // 1.0 * 0.5 = 0.5
    when(repositorioViandasRedistribuidasMock.getTotal(colaboradorMock)).thenReturn(10);  // 10 * 1 = 10
    when(repositorioViandasDonadasMock.getTotal(colaboradorMock)).thenReturn(100);  // 100 * 1.5 = 150
    when(repositorioEntregaTarjetasMock.getTotal(colaboradorMock)).thenReturn(1000);  // 1000 * 2 = 2000
    when(repositorioCuidadoHeladerasMock.getMesesActivosCumulativos(colaboradorMock)).thenReturn(10000);  // 10000 * 5 = 50000

    assertEquals(52160.5, CalculadoraDePuntos.calcular(colaboradorMock));
  }
}