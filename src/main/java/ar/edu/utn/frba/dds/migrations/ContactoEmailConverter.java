package ar.edu.utn.frba.dds.migrations;

import ar.edu.utn.frba.dds.models.entities.contacto.Email;
import com.opencsv.bean.AbstractBeanField;

public class ContactoEmailConverter extends AbstractBeanField<Email, String> {
  @Override
  protected Email convert(String valor) {
    return new Email(valor);
  }
}
