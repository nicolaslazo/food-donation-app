package ar.edu.utn.frba.dds.models.entities;

import ar.edu.utn.frba.dds.models.entities.contribucion.EntregaTarjetas;
import ar.edu.utn.frba.dds.models.entities.personaVulnerable.PersonaVulnerable;
import ar.edu.utn.frba.dds.models.repositories.IPersonaVulnerableRepository;
import ar.edu.utn.frba.dds.models.repositories.ITarjetasAlimentariasRepository;

public class CargadorPersonasVulnerables {
    ITarjetasAlimentariasRepository itarjetasAlimentarias;
    IPersonaVulnerableRepository ipersonaVulnerable;
    public CargadorPersonasVulnerables(ITarjetasAlimentariasRepository itarjetasAlimentarias,
                                       IPersonaVulnerableRepository ipersonaVulnerable) {
        this.itarjetasAlimentarias = itarjetasAlimentarias;
        this.ipersonaVulnerable = ipersonaVulnerable;
    }

    public void cargarPersonasVulnerables(EntregaTarjetas entrega) {
        for( TarjetaAlimentaria tarjetaAlimentaria : entrega.getTarjetasRepartidas() ){
            itarjetasAlimentarias.insert(tarjetaAlimentaria);
            PersonaVulnerable personaVulnerable = tarjetaAlimentaria.recipiente();
            ipersonaVulnerable.insert(personaVulnerable);
        }
    }
}
