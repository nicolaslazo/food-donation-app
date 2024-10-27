package ar.edu.utn.frba.dds.dtos.input.contribucion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;


@AllArgsConstructor
@Getter
public class DistribuirViandaInputDTO {
  @NonNull Long idColaborador;
  @NonNull Long idHeladeraOrigen;
  @NonNull Long idHeladeraDestino;
  @NonNull Integer cantidadViandas;
  @NonNull String motivoDistribucion;
}
