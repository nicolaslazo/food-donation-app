package ar.edu.utn.frba.dds.models.entities.heladera;

import jdk.jfr.Event;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Getter
@Entity
@Table(name = "eventoMovimiento")
public final class EventoMovimiento {

  @Id
  @GeneratedValue
  @Column(name = "id")
  @Getter
  @Setter
  @NonNull Long id;

  @ManyToOne
  @JoinColumn(name = "idHeladera", referencedColumnName = "id")
  @NonNull Heladera heladera;

  @Column(name = "fecha", nullable = false, updatable = false)
  @NonNull ZonedDateTime fecha;

  protected EventoMovimiento()
  {}
  public EventoMovimiento(@NonNull Heladera heladera, @NonNull ZonedDateTime fecha) {
    this.heladera = heladera;
    this.fecha = fecha;
  }

}
