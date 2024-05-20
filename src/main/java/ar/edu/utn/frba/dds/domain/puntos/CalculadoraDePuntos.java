package ar.edu.utn.frba.dds.domain.puntos;

import ar.edu.utn.frba.dds.domain.Heladera;
import ar.edu.utn.frba.dds.domain.colaborador.Colaborador;
import ar.edu.utn.frba.dds.domain.contribucion.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class CalculadoraDePuntos {
    // COEFICIENTES - Depende del archivo properties
    private double coeficientePesosDonados = 0.5;
    private double coeficienteViandasDistribuidas = 1;
    private double coeficienteViandasDonadas = 1.5;
    private double coeficienteTarjetasRepartidas = 2;
    private double coeficienteHeladerasActivas = 5;

    // VARIABLES - Depende de cada Colaborador
    private double pesosDonados;
    private double viandasDistribuidas;
    private int viandasDonadas;
    private double tarjetasRepartidas;
    private int heladerasActivas;
    private double mesesActivas;

    public double calcularPuntos(Colaborador colaborador){
        return (
                obtenerPesosDonados(colaborador) * coeficientePesosDonados +
                obtenerViandasDistribuidas(colaborador) * coeficienteViandasDistribuidas +
                obtenerViandasDonadas(colaborador) * coeficienteViandasDonadas +
                obtenerTarjetasRepartidas(colaborador) * coeficienteTarjetasRepartidas +
                obtenerHeladerasActivas(colaborador) * obtenerMesesActivas(colaborador) * coeficienteHeladerasActivas
        );
    }

    public double obtenerPesosDonados(Colaborador colaborador){
        List<Dinero> contribucionesDinero =
                colaborador.filtrarContribucionesPorTipo(Dinero.class);
        return contribucionesDinero.stream().mapToDouble(Dinero::getMonto).sum();
    }
    public double obtenerViandasDistribuidas(Colaborador colaborador){
        List<RedistribucionViandas> contribucionesRedistribucion =
                colaborador.filtrarContribucionesPorTipo(RedistribucionViandas.class);
        return contribucionesRedistribucion.stream().mapToInt(c -> c.getViandas().size()).sum();
    }

    public double obtenerViandasDonadas(Colaborador colaborador){
        List<DonacionViandas> contribucionesDonacion =
                colaborador.filtrarContribucionesPorTipo(DonacionViandas.class);
        return contribucionesDonacion.stream().mapToInt(c -> c.getViandas().size()).sum();
    }

    public double obtenerTarjetasRepartidas(Colaborador colaborador){
        List<EntregaTarjetas> contribucionesTarjetas =
                colaborador.filtrarContribucionesPorTipo(EntregaTarjetas.class);
        return contribucionesTarjetas.stream().mapToInt(c -> c.getTarjetas().size()).sum();
    }

    public double obtenerHeladerasActivas(Colaborador colaborador){
        List<CuidadoHeladera> contribucionesHeladera =
                colaborador.filtrarContribucionesPorTipo(CuidadoHeladera.class);
        int heladerasActivas = 0;
        for (CuidadoHeladera cuidado :contribucionesHeladera)
        { Heladera heladera = (Heladera) cuidado.getHeladeras();
         if(heladera.isEnAlta())
         {heladerasActivas++;}}
        return heladerasActivas;
    }

    public double obtenerMesesActivas(Colaborador colaborador){
        List<CuidadoHeladera> contribucionesHeladera =
                colaborador.filtrarContribucionesPorTipo(CuidadoHeladera.class);
        float totalMesesActivas = 0;
        ZonedDateTime fechaActual = ZonedDateTime.now();

        for (CuidadoHeladera cuidadoHeladera : contribucionesHeladera) {
            for (Heladera heladera : cuidadoHeladera.getHeladeras()) {
                totalMesesActivas += heladera.mesesActiva();
            }
        }
        return totalMesesActivas;
    }

}

