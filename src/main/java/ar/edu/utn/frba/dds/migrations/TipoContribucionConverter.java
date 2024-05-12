package ar.edu.utn.frba.dds.migrations;

import ar.edu.utn.frba.dds.contribucion.TipoContribucion;
import com.opencsv.bean.AbstractBeanField;

public class TipoContribucionConverter extends AbstractBeanField<TipoContribucion, String> {
  @Override
  protected TipoContribucion convert(String valor) {
    return TipoContribucion.fromString(valor);
  }
}
