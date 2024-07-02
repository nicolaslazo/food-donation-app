package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.Tecnico;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class TecnicoRepository implements ITecnicoRepository {
  static TecnicoRepository instancia = null;
  final List<Tecnico> tecnicos;

  public TecnicoRepository() {
    tecnicos = new ArrayList<>();
  }

  public static TecnicoRepository getInstancia() {
    if (instancia == null) {
      instancia = new TecnicoRepository();
    }

    return instancia;
  }

  public Optional<Tecnico> get(String cuil) {
    return tecnicos.stream().filter(tecnico -> tecnico.getCuil().equals(cuil)).findFirst();
  }

  public void insert(Tecnico tecnico) {
    tecnicos.add(tecnico);
  }

  public boolean delete(String cuil) {
    Optional<Tecnico> tecnicoOptional = get(cuil);
    return tecnicoOptional.map(tecnicos::remove).orElse(false);
  }

  public void deleteTodos() {
    tecnicos.clear();
  }
}
