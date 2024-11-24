package ar.edu.utn.frba.dds.dtos.input.contribucion;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
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
  @NonNull Integer calorias;
  @NonNull Long idHeladera;

  public Vianda getVianda() {
    Colaborador colaborador = new ColaboradorRepository().findById(idColaborador).get();

    return new Vianda(
        descripcion,
        fechaCaducidad,
        fechaDonacion,
        colaborador,
        pesoEnGramos,
        calorias
    );
  }
}
