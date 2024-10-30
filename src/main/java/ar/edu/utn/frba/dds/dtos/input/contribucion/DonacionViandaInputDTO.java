package ar.edu.utn.frba.dds.dtos.input.contribucion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
public class DonacionViandaInputDTO {
  @NonNull Long idColaborador;
  @NonNull String descripcion;
  @NonNull ZonedDateTime fechaCaducidad;
  @NonNull ZonedDateTime fechaDonacion;
  @NonNull Double pesoEnGramos;
  @NonNull Integer caloriasVianda;
  @NonNull Long idHeladera;
}
