package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class CuidadoHeladera extends Contribucion {
  final Heladera heladera;

  public CuidadoHeladera(@NonNull Colaborador colaborador, Heladera heladera) {
    super(colaborador);
    this.heladera = heladera;
  }
}
