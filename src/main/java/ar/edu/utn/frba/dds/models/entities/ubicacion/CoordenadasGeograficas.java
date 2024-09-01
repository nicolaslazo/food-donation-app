package ar.edu.utn.frba.dds.models.entities.ubicacion;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@EqualsAndHashCode
@ToString
public class CoordenadasGeograficas {
  @NonNull Double latitud;
  @NonNull Double longitud;

  public CoordenadasGeograficas(@NonNull Double latitud, @NonNull Double longitud) {
    this.latitud = latitud;
    this.longitud = longitud;
  }

  protected CoordenadasGeograficas() {
  }
}
