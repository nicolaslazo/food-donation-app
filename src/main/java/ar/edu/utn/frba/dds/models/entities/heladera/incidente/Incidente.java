package ar.edu.utn.frba.dds.models.entities.heladera.incidente;

import ar.edu.utn.frba.dds.models.converters.URLAttributeConverter;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.net.URL;
import java.time.ZonedDateTime;

@Entity
@Table(name = "incidente")
public class Incidente {
  @Id
  @GeneratedValue
  @Setter
  @Getter
  long id;

  @ManyToOne
  @JoinColumn(name = "idHeladera", referencedColumnName = "id")
  @Getter
  final @NonNull Heladera heladera;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipoIncidente")
  @Getter
  final @NonNull TipoIncidente tipo;

  @Column(name = "fecha")
  @Getter
  final @NonNull ZonedDateTime fecha;

  @ManyToOne
  @JoinColumn(name = "idColaborador", referencedColumnName = "id")
  final Colaborador colaborador;
  @Column(name = "descripcion", columnDefinition = "TEXT")
  final String descripcion;

  @Column(name="foto")
  @Convert(converter = URLAttributeConverter.class)
  final URL imagen;


  public Incidente(@NonNull Heladera heladera, @NonNull TipoIncidente tipo, @NonNull ZonedDateTime fecha, Colaborador colaborador, String descripcion, URL imagen) {
    this.heladera = heladera;
    this.tipo = tipo;
    this.fecha = fecha;
    this.colaborador = colaborador;
    this.descripcion = descripcion;
    this.imagen = imagen;
  }

  public Incidente(@NonNull Heladera heladera, @NonNull TipoIncidente tipo, @NonNull ZonedDateTime fecha) {
    this(heladera, tipo, fecha, null, null, null);
  }
}
