package ar.edu.utn.frba.dds.contribucion;

import ar.edu.utn.frba.dds.domain.colaborador.Colaborador;

import java.time.LocalDate;

/**
 * Representa una contribución hecha antes de la existencia del sistema. Como se perdió información de estas
 * (datos de viandas, heladeras en las que fueron depositadas, las personas para las que se registraron las tarjetas)
 * no cumplen los estándares de trazabilidad que se esperan de todas las contribuciones de ahora en adelante
 * @param tipo El tipo de contribución hecha. Cada tipo es registrado en los objetos Colaborador de manera distinta
 * @param colaborador El responsable de esta contribución
 * @param fecha La fecha en la que fue ejecutada
 * @param cantidad La cantidad de lo aportado
 */
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