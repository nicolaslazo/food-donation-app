package ar.edu.utn.frba.dds.dtos.input.contribucion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;


@AllArgsConstructor
@Getter
public class DistribuirViandaInputDTO {
  @NonNull Long idColaborador;
  @NonNull Long idHeladeraOrigen;
  @NonNull Long idHeladeraDestino;
  @NonNull List<Long> idsViandas;
  @NonNull String motivoDistribucion;
}
