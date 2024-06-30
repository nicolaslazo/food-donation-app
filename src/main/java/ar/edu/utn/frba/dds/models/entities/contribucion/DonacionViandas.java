package ar.edu.utn.frba.dds.models.entities.contribucion;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import lombok.Getter;

import java.util.Collection;

@Getter
public class DonacionViandas extends MovimientoViandas {
  public DonacionViandas(Colaborador colaborador, Collection<Vianda> viandas, Heladera heladera) {
    super(colaborador, viandas, heladera);
  }

  @Override
  public String toString() {
    return "DonacionViandas{" +
        "colaborador=" + colaborador +
        ", fechaRealizada=" + fechaRealizada +
        ", cantidadViandas=" + viandas.size() +
        '}';
  }
}
