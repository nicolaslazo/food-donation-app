package ar.edu.utn.frba.dds.dtos.input.contribucion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class EntregaTarjetasInputDTO {
  // Inputs Obligatorios
  @NonNull Long idColaborador;
  @NonNull Integer tarjetasSolicitadas;

  // Input de Dirección de Entrega no obligatorio
  String pais;
  String provincia;
  String ciudad;
  String codigoPostal;
  String calle;
  String altura;
  String unidad;
  String piso;

  public EntregaTarjetasInputDTO(
          @NonNull Long idColaborador,
          @NonNull Integer tarjetasSolicitadas,
          String pais,
          String provincia,
          String ciudad,
          String codigoPostal,
          String calle,
          String altura,
          String unidad,
          String piso
  ) {
    this.idColaborador = idColaborador;
    this.tarjetasSolicitadas = tarjetasSolicitadas;
    this.pais = pais;
    this.provincia = provincia;
    this.ciudad = ciudad;
    this.codigoPostal = codigoPostal;
    this.calle = calle;
    this.altura = altura;
    this.unidad = unidad;
    this.piso = piso;
  }
}
