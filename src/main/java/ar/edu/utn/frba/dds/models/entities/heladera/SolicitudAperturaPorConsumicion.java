package ar.edu.utn.frba.dds.models.entities.heladera;

import ar.edu.utn.frba.dds.config.ConfigLoader;
import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import lombok.Getter;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(name = "solicitudAperturaPorConsumicion")
@Getter
public class SolicitudAperturaPorConsumicion {
  @Column(name = "id", nullable = false, unique = true, updatable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  Long id;

  @ManyToOne(targetEntity = Tarjeta.class)
  @JoinColumn(name = "idTarjeta", nullable = false)
  @NonNull Tarjeta tarjeta;

  @ManyToOne(targetEntity = Vianda.class)
  @JoinColumn(name = "idVianda", nullable = false, referencedColumnName = "id")
  @NonNull Vianda vianda;

  @ManyToOne(targetEntity = Heladera.class)
  @JoinColumn(name = "idHeladera", nullable = false, updatable = false, referencedColumnName = "id")
  @NonNull Heladera heladera;

  @Column(name = "fechaCreacion", nullable = false, updatable = false)
  @NonNull ZonedDateTime fechaCreacion;

  @Column(name = "fechaVencimiento", nullable = false, updatable = false)
  @NonNull ZonedDateTime fechaVencimiento;

  @Column(name = "fechaUsada")
  ZonedDateTime fechaUsada = null;

  public SolicitudAperturaPorConsumicion(@NonNull Tarjeta tarjeta,
                                         @NonNull Vianda vianda,
                                         @NonNull ZonedDateTime fechaCreacion) {
    this.tarjeta = tarjeta;
    this.vianda = vianda;
    this.heladera = vianda.getHeladera();
    this.fechaCreacion = fechaCreacion;

    int tiempoSolicitudAperturaMinutos =
        Integer.parseInt(ConfigLoader.getInstancia().getProperty("heladeras.tiempoSolicitudAperturaMinutos"));

    this.fechaVencimiento = fechaCreacion.plusMinutes(tiempoSolicitudAperturaMinutos);
  }

  protected SolicitudAperturaPorConsumicion() {
  }

  void verificarPrecondicionesApertura(ZonedDateTime timestamp) throws SolicitudInvalidaException {
    if (fechaUsada != null)
      throw new SolicitudInvalidaException("Esta solicitud ya fue usada en" + fechaUsada);
    if (timestamp.isAfter(fechaVencimiento)) throw new SolicitudInvalidaException("Esta solicitud ya venció");
    if (timestamp.isBefore(fechaCreacion))
      throw new SolicitudInvalidaException("No se debería poder usar una solicitud antes de que entre en vigencia");
  }

  public void setFechaUsada(ZonedDateTime timestamp) throws SolicitudInvalidaException {
    verificarPrecondicionesApertura(timestamp);

    vianda.setHeladera(null);
    fechaUsada = timestamp;
  }

  public double getPesoViandaEnGramos() {
    return vianda.getPesoEnGramos();
  }

  public boolean isVigenteAlMomento(ZonedDateTime momento) {
    return fechaUsada == null && momento.isAfter(fechaCreacion) && momento.isBefore(fechaVencimiento);
  }
}
