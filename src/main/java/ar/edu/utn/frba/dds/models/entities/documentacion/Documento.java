package ar.edu.utn.frba.dds.models.entities.documentacion;

import lombok.Getter;
import lombok.NonNull;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Getter
public class Documento {
  @NonNull TipoDocumento tipo;
  @NonNull Integer valor;

  public Documento(@NonNull TipoDocumento tipo, @NonNull Integer valor) {
    this.tipo = tipo;
    this.valor = valor;
  }

  protected Documento() {
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (Documento) obj;
    return Objects.equals(this.tipo, that.tipo) &&
        Objects.equals(this.valor, that.valor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tipo, valor);
  }

  @Override
  public String toString() {
    return "Documento[" +
        "tipo=" + tipo + ", " +
        "valor=" + valor + ']';
  }
}