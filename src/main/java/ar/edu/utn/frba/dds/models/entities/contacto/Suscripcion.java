package ar.edu.utn.frba.dds.models.entities.contacto;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.MotivoDeDistribucion;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class Suscripcion {
  @Getter
  final @NonNull Heladera heladera;
  @Getter
  final @NonNull MotivoDeDistribucion tipo;
  final Integer parametro;
  @Getter
  final @NonNull Colaborador colaborador;
  @Getter
  @Setter
  int id;

  public Suscripcion(@NonNull Heladera heladera,
                     @NonNull MotivoDeDistribucion tipo,
                     Integer parametro,
                     @NonNull Colaborador colaborador) {
    this.heladera = heladera;
    this.tipo = tipo;
    this.parametro = parametro;
    this.colaborador = colaborador;
  }
}
