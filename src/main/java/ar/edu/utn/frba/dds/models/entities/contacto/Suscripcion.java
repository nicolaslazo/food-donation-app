package ar.edu.utn.frba.dds.models.entities.contacto;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import lombok.Data;

@Data
public class Suscripcion {
  private final Heladera heladera;
  private final TipoNotificacion tipoNotificacion;
  private final Colaborador colaborador;
  private int id;

  public String getTopic() {
    return "heladera/%s/%s".formatted(heladera.getId(), tipoNotificacion.toString());
  }
}
