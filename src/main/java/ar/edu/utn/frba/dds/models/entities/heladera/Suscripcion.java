package ar.edu.utn.frba.dds.models.entities.heladera;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import lombok.Data;

@Data
public class Suscripcion {
  private final Heladera heladera;
  private final Colaborador colaborador;
  private int id;
}
