package ar.edu.utn.frba.dds.models.entities.heladera.incidente;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.net.URL;
import java.time.ZonedDateTime;

@Getter
@Entity
@Table(name = "incidente")
public class Incidente {
  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false, unique = true, updatable = false)
  Long id;

  @ManyToOne(targetEntity = Heladera.class)
  @JoinColumn(name = "idHeladera", referencedColumnName = "id", nullable = false, updatable = false)
  @NonNull Heladera heladera;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo", nullable = false, updatable = false)
  @NonNull TipoIncidente tipo;

  @Column(name = "fecha", nullable = false, updatable = false)
  @NonNull ZonedDateTime fecha;

  @ManyToOne(fetch = FetchType.LAZY, targetEntity = Colaborador.class)
  @JoinColumn(name = "idColaborador", referencedColumnName = "idUsuario", updatable = false)
  Colaborador colaborador;

  @Column(name = "descripcion", updatable = false)
  String descripcion;

  @Column(name = "imagen", updatable = false)
  URL imagen;

  @Column(name = "fechaResuelto")
  @Setter // Si es Null, se entiende que no fue resuelto el incidente
  ZonedDateTime fechaResuelto;

  public Incidente(@NonNull Heladera heladera,
                   @NonNull TipoIncidente tipo,
                   @NonNull ZonedDateTime fecha,
                   Colaborador colaborador,
                   String descripcion,
                   URL imagen) {
    this.heladera = heladera;
    this.tipo = tipo;
    this.fecha = fecha;
    this.colaborador = colaborador;
    this.descripcion = descripcion;
    this.imagen = imagen;
  }

  protected Incidente() {
  }

  public Incidente(@NonNull Heladera heladera, @NonNull TipoIncidente tipo, @NonNull ZonedDateTime fecha) {
    this(heladera, tipo, fecha, null, null, null);
  }
}
