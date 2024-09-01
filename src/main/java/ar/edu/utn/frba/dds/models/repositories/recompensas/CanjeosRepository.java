package ar.edu.utn.frba.dds.models.repositories.recompensas;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.recompensas.CalculadoraDePuntos;
import ar.edu.utn.frba.dds.models.entities.recompensas.Canjeo;
import ar.edu.utn.frba.dds.models.entities.recompensas.ExcepcionDeCanjeDePuntos;
import ar.edu.utn.frba.dds.models.entities.recompensas.Recompensa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class CanjeosRepository {
  private final List<Canjeo> canjeos;

  public CanjeosRepository() {
    canjeos = new ArrayList<>();
  }

  public Optional<Canjeo> get(int id) {
    return canjeos.stream().filter(canjeo -> canjeo.getId() == id).findFirst();
  }

  public Stream<Canjeo> getParaColaborador(Colaborador colaborador) {
    return canjeos.stream().filter(canjeo -> canjeo.getColaborador() == colaborador);
  }

  public double getPuntosDisponibles(Colaborador colaborador) {
    /* TODO: Esto sería el equivalente de una derived column. Hay que almacenar las contribuciones en su propio
             repositorio. Está bien tener métodos no convencionales como este en un repository pattern?
     */
    double puntosTotales = CalculadoraDePuntos.calcular(colaborador);
    double puntosGastados = getParaColaborador(colaborador)
        .map(Canjeo::getRecompensa)
        .mapToDouble(Recompensa::getCostoEnPuntos)
        .sum();

    return puntosTotales - puntosGastados;
  }

  public Stream<Canjeo> getParaRecompensa(Recompensa recompensa) {
    return canjeos.stream().filter(canjeo -> canjeo.getRecompensa().getId() == recompensa.getId());
  }

  public long getStockDisponible(Recompensa recompensa) {
    return recompensa.getStockInicial() - getParaRecompensa(recompensa).count();
  }

  public void insert(Canjeo canjeo) throws ExcepcionDeCanjeDePuntos {
    Colaborador colaborador = canjeo.getColaborador();
    if (getPuntosDisponibles(colaborador) < canjeo.getRecompensa().getCostoEnPuntos()) {
      throw new ExcepcionDeCanjeDePuntos("Colaborador no posee los puntos necesarios para la transacción");
    }
    if (getStockDisponible(canjeo.getRecompensa()) <= 0) {
      throw new ExcepcionDeCanjeDePuntos("Recompensa no posee el stock necesario para la transacción");
    }

    canjeo.setId(canjeos.size() + 1);
    canjeos.add(canjeo);
  }
}
