package ar.edu.utn.frba.dds.models.repositories.documentacion;

import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;


import java.util.Optional;
import java.util.UUID;

public class TarjetasRepository extends HibernateEntityManager<Tarjeta, UUID> {
  public Optional<Tarjeta> getVigentePara(Usuario usuario) {
    String criteria = "AND fecha_alta IS NOT NULL AND fecha_baja IS NULL";
    return findFirstByCriteria("recipiente", usuario, criteria);
  }

}
