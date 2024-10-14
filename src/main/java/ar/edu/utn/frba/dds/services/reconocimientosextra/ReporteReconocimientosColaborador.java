package ar.edu.utn.frba.dds.services.reconocimientosextra;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Getter
public class ReporteReconocimientosColaborador {
  long id;
  @NonNull String primerNombre;
  @NonNull String apellido;
  @NonNull String contacto;
  float puntos;
}
