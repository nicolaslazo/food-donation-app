package ar.edu.utn.frba.dds.models.repositories.recompensas;

import java.util.List;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.recompensas.Recompensa;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;

public class RecompensasRepository extends HibernateEntityManager<Recompensa, Long> {

    public List<Recompensa> getAllMyRecompensas(Colaborador colaborador) {
        return this.findAll().filter(r -> r.getProveedor() == colaborador).toList();
    }
}
