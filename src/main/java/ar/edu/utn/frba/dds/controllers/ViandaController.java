package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ViandaController {
  public void assertViandasSonDeLaMismaHeladera(Collection<Vianda> viandas) {
    final Set<Heladera> heladerasInvolucradas = new HashSet<>(viandas.stream().map(Vianda::getHeladera).toList());
    if (heladerasInvolucradas.size() > 1)
      throw new RuntimeException("No se pueden insertar viandas de heladeras distintas en la misma transacción");
  }
}
