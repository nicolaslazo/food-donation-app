package ar.edu.utn.frba.dds.auth.validadorContrasenia;

import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

public class ValidadorDeContrasenia {
  private Set<FiltrosContrasenia> filtros; /*Va a contener todos los filtros que queremos aplicar*/

  public ValidadorDeContrasenia()
  { filtros = new HashSet<>();
    filtros.add(new FiltroEstandarContraseniaNoPopular());
    filtros.add(new FiltroEstandarLongitud());
    filtros.add(new FiltroEstandarNoTieneUnicode());
  }

  public boolean validar(@NonNull String contrasenia) {
    for (FiltrosContrasenia filtro : filtros)
    {if (!filtro.validar(contrasenia)) {
        return false;}}
  return true;}

}
