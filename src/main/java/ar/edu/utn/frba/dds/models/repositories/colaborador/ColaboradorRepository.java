package ar.edu.utn.frba.dds.models.repositories.colaborador;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.repositories.HibernateEntityManager;

import java.util.UUID;

public class ColaboradorRepository extends HibernateEntityManager<Colaborador, UUID> {
  static ColaboradorRepository instancia = null;

  private ColaboradorRepository() {
  }

  public static ColaboradorRepository getInstancia() {
    if (instancia == null) instancia = new ColaboradorRepository();

    return instancia;
  }
}