package ar.edu.utn.frba.dds.models.entities.recompensas;

import ar.edu.utn.frba.dds.config.ConfigLoader;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.repositories.contribucion.CuidadoHeladerasRepository;
import ar.edu.utn.frba.dds.models.repositories.contribucion.DineroRepository;
import ar.edu.utn.frba.dds.models.repositories.contribucion.DonacionViandasRepository;
import ar.edu.utn.frba.dds.models.repositories.contribucion.EntregaTarjetasRepository;
import ar.edu.utn.frba.dds.models.repositories.contribucion.RedistribucionViandasRepository;

public class CalculadoraDePuntos {
  private static final ConfigLoader coeficientesConfig = ConfigLoader.getInstancia();
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

  public static long calcular(Colaborador colaborador) {
    return (long) (new DineroRepository().getTotal(colaborador) * coeficientePesosDonados +
        RedistribucionViandasRepository.getInstancia().getTotal(colaborador) * coeficienteViandasDistribuidas +
        (new DonacionViandasRepository().getTotal(colaborador)) * coeficienteViandasDonadas +
        EntregaTarjetasRepository.getInstancia().getTotal(colaborador) * coeficienteTarjetasRepartidas +
        CuidadoHeladerasRepository.getInstancia().getMesesActivosCumulativos(colaborador) * coeficienteHeladerasActivas);
  }
}

