package ar.edu.utn.frba.dds.domain.recompensas;

import ar.edu.utn.frba.dds.config.ConfigLoader;
import ar.edu.utn.frba.dds.domain.colaborador.Colaborador;

// TODO: Hay alguna manera de hacerla estática? Nunca vamos a necesitar múltiples instancias de esta clase
public class CalculadoraDePuntos {
  private static final ConfigLoader coeficientesConfig =
      new ConfigLoader("application.properties");
  private static final double coeficientePesosDonados =
      Double.parseDouble(coeficientesConfig.getProperty("puntaje.coeficiente.pesosDonados"));
  private static final double coeficienteViandasDistribuidas =
      Double.parseDouble(coeficientesConfig.getProperty("puntaje.coeficiente.viandasDistribuidas"));
  private static final double coeficienteViandasDonadas =
      Double.parseDouble(coeficientesConfig.getProperty("puntaje.coeficiente.viandasDonadas"));
  private static final double coeficienteTarjetasRepartidas =
      Double.parseDouble(coeficientesConfig.getProperty("puntaje.coeficiente.tarjetasRepartidas"));
  private static final double coeficienteHeladerasActivas =
      Double.parseDouble(coeficientesConfig.getProperty("puntaje.coeficiente.heladerasActivas"));

  public static double calcularPuntos(Colaborador colaborador) {
    return (
        colaborador.getDineroDonado() * coeficientePesosDonados +
            colaborador.getNumeroViandasDistribuidas() * coeficienteViandasDistribuidas +
            colaborador.getNumeroViandasDonadas() * coeficienteViandasDonadas +
            colaborador.getNumeroTarjetasRepartidas() * coeficienteTarjetasRepartidas +
            colaborador.getMesesCumulativosCuidadoHeladeras() * coeficienteHeladerasActivas
    );
  }
}

