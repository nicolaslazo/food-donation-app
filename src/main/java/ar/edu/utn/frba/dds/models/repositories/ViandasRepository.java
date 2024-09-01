package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.Vianda;
import ar.edu.utn.frba.dds.models.entities.heladera.Heladera;
import ar.edu.utn.frba.dds.models.repositories.heladera.HeladerasRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ViandasRepository {
  static ViandasRepository instancia = null;
  final List<Vianda> viandas;

  private ViandasRepository() {
    viandas = new ArrayList<>();
  }

  public static ViandasRepository getInstancia() {
    if (instancia == null) instancia = new ViandasRepository();

    return instancia;
  }

  public Optional<Vianda> get(int id) {
    return viandas.stream().filter(vianda -> vianda.getId() == id).findFirst();
  }

  public List<Vianda> getTodas() {
    return viandas;
  }

  public List<Vianda> getAlmacenadas(Heladera heladera) {
    return viandas.stream().filter(vianda -> vianda.getHeladera() == heladera).toList();
  }

  private void assertHeladeraTieneSuficienteEspacio(Heladera destino, int cantidadViandas) throws RepositoryException {
    final int capacidadDisponible = HeladerasRepository.getInstancia().getCapacidadDisponible(destino);
    final String sPlural = cantidadViandas > 1 ? "s" : "";

    if (capacidadDisponible < cantidadViandas)
      throw new RepositoryException(
          "La heladera con id " +
              destino.getId() +
              " no posee el espacio para esta" +
              sPlural +
              " vianda" +
              sPlural +
              ". Requerido: " +
              cantidadViandas +
              ". Disponible: " +
              capacidadDisponible);
  }

  private void assertViandasSonDeLaMismaHeladera(Collection<Vianda> viandas) throws RepositoryException {
    final Set<Heladera> heladerasInvolucradas = new HashSet<>(viandas.stream().map(Vianda::getHeladera).toList());
    if (heladerasInvolucradas.size() > 1)
      throw new RepositoryException("No se pueden insertar viandas de heladeras distintas en la misma transacción");
  }

  public void insert(Collection<Vianda> viandas) throws RepositoryException {
    // Diseñada para calcular la disponibilidad de espacio en una heladera sola.
    // No hay caso de uso que justifique complicarla
    assertViandasSonDeLaMismaHeladera(viandas);

    final Heladera heladeraInvolucrada = viandas.iterator().next().getHeladera();
    if (heladeraInvolucrada != null) assertHeladeraTieneSuficienteEspacio(heladeraInvolucrada, viandas.size());

    for (Vianda vianda : viandas) {
      insert(vianda);
    }
  }

  public int insert(Vianda vianda) throws RepositoryException {
    if (vianda.getHeladera() != null) assertHeladeraTieneSuficienteEspacio(vianda.getHeladera(), 1);

    viandas.add(vianda);
    vianda.setId(viandas.size());

    return vianda.getId();
  }

  public void updateUbicacion(Vianda vianda, Heladera ubicacionNueva) throws RepositoryException {
    assertHeladeraTieneSuficienteEspacio(ubicacionNueva, 1);

    vianda.setHeladera(ubicacionNueva);
  }

  // TODO: Necesitamos este método? Lo podemos aplicar en las concretizaciones de contribuciones?
  public void updateUbicacion(Collection<Vianda> viandas, Heladera ubicacionNueva) throws RepositoryException {
    // No hay ninguna razón por la que podamos mover viandas de una heladera a otra
    // afuera del marco de las contribuciones, donde todas las viandas tienen el mismo origen
    assertViandasSonDeLaMismaHeladera(viandas);
    assertHeladeraTieneSuficienteEspacio(ubicacionNueva, viandas.size());

    for (Vianda vianda : viandas) {
      updateUbicacion(vianda, ubicacionNueva);
    }
  }

  public void deleteTodas() {
    viandas.clear();
  }
}
