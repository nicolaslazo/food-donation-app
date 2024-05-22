package ar.edu.utn.frba.dds.migrations;

import ar.edu.utn.frba.dds.domain.documentacion.TipoDocumento;
import com.opencsv.bean.AbstractBeanField;

public final class TipoDocumentoConverter extends AbstractBeanField<TipoDocumento, String> {
  @Override
  protected TipoDocumento convert(String valor) {
    return TipoDocumento.fromString(valor);
  }
}
