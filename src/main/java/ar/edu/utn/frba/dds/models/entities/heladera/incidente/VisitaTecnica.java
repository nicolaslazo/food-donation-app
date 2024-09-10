package ar.edu.utn.frba.dds.models.entities.heladera.incidente;

import ar.edu.utn.frba.dds.models.entities.Tecnico;
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
import java.net.URL;
import java.time.ZonedDateTime;

@Entity
@Table(name = "visitaTecnica")
@Getter
public class VisitaTecnica {

  @ManyToOne
  @JoinColumn(name = "idTecnico", nullable = false, referencedColumnName = "idUsuario")
  @NonNull
  Tecnico tecnico;

  @ManyToOne
  @JoinColumn(name = "idIncidente", nullable = false, referencedColumnName = "id")
  @Getter
  @NonNull Incidente incidente;

  @Column(name = "fecha", nullable = false)
  @NonNull ZonedDateTime fecha;

  @Column(name = "incidenteResuelto")
  @Getter
  boolean incidenteResuelto;

  @Column(name = "descripcion", length = 1000)
  String descripcion;

  @Column(name = "foto")
  URL imagen;

  @Id
  @GeneratedValue
  @Getter
  @Setter
  long id;

  public VisitaTecnica(@NonNull Tecnico tecnico,
                       @NonNull Incidente incidente,
                       @NonNull ZonedDateTime fecha,
                       boolean incidenteResuelto,
                       String descripcion,
                       URL imagen) {
    this.tecnico = tecnico;
    this.incidente = incidente;
    this.fecha = fecha;
    this.incidenteResuelto = incidenteResuelto;
    this.descripcion = descripcion;
    this.imagen = imagen;
  }

  protected VisitaTecnica(){

  }
  public VisitaTecnica(@NonNull Tecnico tecnico,
                       @NonNull Incidente incidente,
                       @NonNull ZonedDateTime fecha,
                       boolean incidenteResuelto) {
    this(tecnico, incidente, fecha, incidenteResuelto, null, null);
  }
}
