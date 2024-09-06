package ar.edu.utn.frba.dds.models.entities.heladera.incidente;

import ar.edu.utn.frba.dds.models.converters.URLAttributeConverter;
import ar.edu.utn.frba.dds.models.entities.Tecnico;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.net.URL;
import java.time.ZonedDateTime;

@Entity
@Table(name="visitaTecnica")
@Getter
public class VisitaTecnica {

  @Transient
  //@ManyToOne
  //@JoinColumn(name = "idTecnico", nullable = false, referencedColumnName = "id")
  //@NonNull
  final Tecnico tecnico;

  @ManyToOne
  @JoinColumn(name = "idIncidente", nullable = false, referencedColumnName = "id")
  @Getter
  final @NonNull Incidente incidente;

  @Column(name = "fecha", nullable = false)
  final @NonNull ZonedDateTime fecha;

  @Column(name = "incidenteResuelto")
  @Getter
  final boolean incidenteResuelto;

  @Column(name = "descripcion", length = 1000)
  final String descripcion;

  @Column(name = "foto")
  @Convert(converter = URLAttributeConverter.class)
  final URL imagen;

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

  public VisitaTecnica(@NonNull Tecnico tecnico,
                       @NonNull Incidente incidente,
                       @NonNull ZonedDateTime fecha,
                       boolean incidenteResuelto) {
    this(tecnico, incidente, fecha, incidenteResuelto, null, null);
  }
}
