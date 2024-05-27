package ar.edu.utn.frba.dds.models.entities.recompensas;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculadoraDePuntosTest {
  @Mock
  Colaborador colaboradorDummy;

  @Test
  public void calculaPuntaje() {
    when(colaboradorDummy.getNumeroViandasDistribuidas()).thenReturn(1);  // 1 * 1 = 1
    when(colaboradorDummy.getNumeroViandasDonadas()).thenReturn(10);  // 10 * 1.5 = 15
    when(colaboradorDummy.getNumeroTarjetasRepartidas()).thenReturn(100);  // 100 * 2 = 200
    when(colaboradorDummy.getMesesCumulativosCuidadoHeladeras()).thenReturn(1000);  // 1000 * 5 = 5000

    assertEquals(5216, CalculadoraDePuntos.calcular(colaboradorDummy));
  }
}