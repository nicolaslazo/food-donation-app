package ar.edu.utn.frba.dds.migrations;

import ar.edu.utn.frba.dds.models.entities.contacto.ContactoEmail;
import com.opencsv.bean.AbstractBeanField;

public class ContactoEmailConverter extends AbstractBeanField<ContactoEmail, String> {
  @Override
  protected ContactoEmail convert(String valor) {
    return new ContactoEmail(valor);
  }
}
