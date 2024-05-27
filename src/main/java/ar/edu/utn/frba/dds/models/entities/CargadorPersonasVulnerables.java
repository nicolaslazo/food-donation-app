package ar.edu.utn.frba.dds.models.entities;

import ar.edu.utn.frba.dds.models.entities.contribucion.EntregaTarjetas;
import ar.edu.utn.frba.dds.models.repositories.ITarjetasAlimentariasRepository;

public class CargadorPersonasVulnerables {
    ITarjetasAlimentariasRepository itarjetasAlimentarias;

    public CargadorPersonasVulnerables(ITarjetasAlimentariasRepository itarjetasAlimentarias) {
        this.itarjetasAlimentarias = itarjetasAlimentarias;
    }

    public void cargarPersonasVulnerables(EntregaTarjetas entrega) {
        for(TarjetaAlimentaria tarjetaAlimentaria : entrega.getTarjetasRepartidas()){
            itarjetasAlimentarias.insert(tarjetaAlimentaria);
        }
    }
}
