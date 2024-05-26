package ar.edu.utn.frba.dds.domain.recompensas;

import ar.edu.utn.frba.dds.domain.colaborador.Colaborador;

public class CalculadoraDePuntos {
  // COEFICIENTES - Depende del archivo properties
  // TODO: Cargar desde el properties
  private double coeficientePesosDonados = 0.5;
  private double coeficienteViandasDistribuidas = 1;
  private double coeficienteViandasDonadas = 1.5;
  private double coeficienteTarjetasRepartidas = 2;
  private double coeficienteHeladerasActivas = 5;

  public double calcularPuntos(Colaborador colaborador) {
    return (
        colaborador.getDineroDonado() * coeficientePesosDonados +
            colaborador.getNumeroViandasDistribuidas() * coeficienteViandasDistribuidas +
            colaborador.getNumeroViandasDonadas() * coeficienteViandasDonadas +
            colaborador.getNumeroTarjetasRepartidas() * coeficienteTarjetasRepartidas +
            colaborador.getMesesCumulativosCuidadoHeladeras() * coeficienteHeladerasActivas
    );
  }
}

