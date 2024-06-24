package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.Tecnico;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class TecnicoRepository implements ITecnicoRepository {
  private static TecnicoRepository instancia = null;
  private final List<Tecnico> tecnicos;

  public TecnicoRepository() {
    tecnicos = new ArrayList<>();
  }

  public static TecnicoRepository getInstancia() {
    if (instancia == null) {
      instancia = new TecnicoRepository();
    }

    return instancia;
  }

  public Optional<Tecnico> getTecnico(String cuil) {
    return tecnicos
        .stream()
        .filter(tecnico -> tecnico.getCuil().equals(cuil))
        .findFirst();
  }

  public void insertTecnico(Tecnico tecnico) {
    tecnicos.add(tecnico);
  }

  public boolean deleteTecnico(String cuil) {
    Optional<Tecnico> tecnicoOptional = getTecnico(cuil);
    return tecnicoOptional.map(tecnicos::remove).orElse(false);
  }

  public boolean updateTecnico(Tecnico tecnico) {
    //TODO
    return true;
  }

  public void deleteTodos() {
    tecnicos.clear();
  }
}
