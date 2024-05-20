package ar.edu.utn.frba.dds.domain.puntos;

import ar.edu.utn.frba.dds.domain.colaborador.Colaborador;

public class CalculadoraDePuntos {
    // COEFICIENTES - Depende del archivo properties
    private float coeficientePesosDonados;
    private float coeficienteViandasDistribuidas;
    private float coeficienteViandasDonadas;
    private float coeficienteTarjetasRepartidas;
    private float coeficienteHeladerasActivas;

    // VARIABLES - Depende de cada Colaborador
    private float pesosDonados;
    private float viandasDistribuidas;
    private int viandasDonadas;
    private float tarjetasRepartidas;
    private int heladerasActivas;
    private float mesesActivas;

    public float calcularPuntos(Colaborador colaborador){
        return (
                obtenerPesosDonados(colaborador) * coeficientePesosDonados +
                obtenerViandasDistribuidas(colaborador) * coeficienteViandasDistribuidas +
                obtenerViandasDonados(colaborador) * coeficienteViandasDonadas +
                obtenerTarjetasRepartidas(colaborador) * coeficienteTarjetasRepartidas +
                obtenerHeladerasActivas(colaborador) * obtenerMesesActivos(colaborador) * coeficienteHeladerasActivas
        );
    }

    private float obtenerMesesActivos(Colaborador colaborador) {
        return 0;
    }

    private float obtenerHeladerasActivas(Colaborador colaborador) {
        return 0;
    }

    private float obtenerTarjetasRepartidas(Colaborador colaborador) {
        return 0;
    }

    private float obtenerViandasDistribuidas(Colaborador colaborador) {
        return 0;
    }

    private float obtenerViandasDonados(Colaborador colaborador) {
        return 0;
    }

    public float obtenerPesosDonados(Colaborador colaborador){
        return 0;
    }
}
