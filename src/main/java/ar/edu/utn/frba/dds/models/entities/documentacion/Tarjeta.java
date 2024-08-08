package ar.edu.utn.frba.dds.models.entities.documentacion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.users.PermisoDenegadoException;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import lombok.Getter;
import lombok.NonNull;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Tarjeta {
  final @NonNull UUID id;
  Colaborador proveedor = null;
  Usuario recipiente = null;
  ZonedDateTime fechaAlta = null;
  ZonedDateTime fechaBaja = null;
  Usuario responsableDeBaja = null;

  public Tarjeta(@NonNull UUID id) {
    this.id = id;
  }

  public void assertTienePermiso(@NonNull String nombrePermiso, @NonNull String razon) throws PermisoDenegadoException {
    assertEstaVigente();

    recipiente.assertTienePermiso(nombrePermiso, razon);
  }

  void assertEstaVigente() throws PermisoDenegadoException {
    if (fechaBaja != null) throw new PermisoDenegadoException("Esta tarjeta ya fue dada de baja");
    if (fechaAlta == null) throw new PermisoDenegadoException("Esta tarjeta no fue dada de alta");
  }

  public void setEnAlta(@NonNull Usuario recipiente, @NonNull Colaborador proveedor, @NonNull ZonedDateTime timestamp)
      throws PermisoDenegadoException {
    if (this.fechaAlta != null) throw new PermisoDenegadoException("Esta tarjeta ya está en alta");

    this.recipiente = recipiente;
    this.proveedor = proveedor;
    this.fechaAlta = timestamp;
  }

  public void setDeBaja(@NonNull Usuario quien, @NonNull ZonedDateTime timestamp) throws PermisoDenegadoException {
    assertEstaVigente();

    responsableDeBaja = quien;
    fechaBaja = timestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Tarjeta that = (Tarjeta) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
