package ar.edu.utn.frba.dds.models.entities.recompensas;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.repositories.contribucion.CuidadoHeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.contribucion.DineroRepository;
import ar.edu.utn.frba.dds.models.repositories.contribucion.DonacionViandasRepository;
import ar.edu.utn.frba.dds.models.repositories.contribucion.EntregaTarjetasRepository;
import ar.edu.utn.frba.dds.models.repositories.contribucion.RedistribucionViandasRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculadoraDePuntosTest {
  @Mock
  Colaborador colaboradorMock;

  private MockedConstruction<DineroRepository> dineroRepositoryMock;
  private MockedConstruction<RedistribucionViandasRepository> redistribucionViandasRepositoryMock;
  private MockedConstruction<DonacionViandasRepository> donacionViandasRepositoryMock;
  private MockedConstruction<EntregaTarjetasRepository> entregaTarjetasRepositoryMock;
  private MockedConstruction<CuidadoHeladerasRepository> cuidadoHeladerasRepositoryMock;

  @BeforeEach
  void setUp() {
    dineroRepositoryMock = mockConstruction(DineroRepository.class,
        (mock, context) -> when(mock.getTotal(colaboradorMock)).thenReturn(2.));

    redistribucionViandasRepositoryMock = mockConstruction(RedistribucionViandasRepository.class,
        (mock, context) -> when(mock.getTotal(colaboradorMock)).thenReturn(10));

    donacionViandasRepositoryMock = mockConstruction(DonacionViandasRepository.class,
        (mock, context) -> when(mock.getTotal(colaboradorMock)).thenReturn(100));

    entregaTarjetasRepositoryMock = mockConstruction(EntregaTarjetasRepository.class,
        (mock, context) -> when(mock.getTotal(colaboradorMock)).thenReturn(1000));

    cuidadoHeladerasRepositoryMock = mockConstruction(CuidadoHeladerasRepository.class,
        (mock, context) -> when(mock.getMesesActivosCumulativos(colaboradorMock)).thenReturn(10000L));
  }

  @AfterEach
  void tearDown() {
    dineroRepositoryMock.close();
    redistribucionViandasRepositoryMock.close();
    donacionViandasRepositoryMock.close();
    entregaTarjetasRepositoryMock.close();
    cuidadoHeladerasRepositoryMock.close();
  }

  @Test
  public void testCalculaPuntaje() {
    assertEquals(52161, CalculadoraDePuntos.calcular(colaboradorMock));
  }
}
