package ar.edu.utn.frba.dds.contribucion;

import ar.edu.utn.frba.dds.domain.colaborador.Colaborador;

import java.time.LocalDate;

public record ContribucionLegacy(TipoContribucion tipo, Colaborador colaborador, LocalDate fecha, int cantidad) {
  public ContribucionLegacy(TipoContribucion tipo, Colaborador colaborador, LocalDate fecha, int cantidad) {
    this.tipo = tipo;
    this.colaborador = colaborador;
    this.fecha = fecha;
    this.cantidad = cantidad;

    switch (tipo) {
      case DINERO -> colaborador.registrarColaboracionDinero(cantidad);
      case DONACION_VIANDAS -> colaborador.registrarColaboracionViandas(cantidad);
      case ENTREGA_TARJETAS -> colaborador.registrarColaboracionEntregaTarjetas(cantidad);
      case REDISTRIBUCION_VIANDAS -> colaborador.registrarColaboracionRedistribucionViandas(cantidad);
    }
  }
}