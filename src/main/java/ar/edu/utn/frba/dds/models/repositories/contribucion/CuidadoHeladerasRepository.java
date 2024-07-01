package ar.edu.utn.frba.dds.models.repositories.contribucion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contribucion.CuidadoHeladera;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CuidadoHeladerasRepository {
  static CuidadoHeladerasRepository instancia = null;
  final List<CuidadoHeladera> contribuciones;

  public CuidadoHeladerasRepository() {
    contribuciones = new ArrayList<>();
  }

  public static CuidadoHeladerasRepository getInstancia() {
    if (instancia == null) {
      instancia = new CuidadoHeladerasRepository();
    }

    return instancia;
  }

  public Optional<CuidadoHeladera> get(int id) {
    return contribuciones.stream().filter(contribucion -> contribucion.getId() == id).findFirst();
  }

  public int insert(CuidadoHeladera contribucion) throws RepositoryException {
    if (contribuciones.stream().map(CuidadoHeladera::getHeladera).anyMatch(heladera -> heladera == contribucion.getHeladera())) {
      throw new RepositoryException("Esa heladera ya fue insertada en otra contribución");
    }

    contribuciones.add(contribucion);
    contribucion.setId(contribuciones.size());

    return contribucion.getId();
  }

  public int getMesesActivosCumulativos(Colaborador colaborador) {
    return contribuciones
            .stream()
            .filter(contribucion -> contribucion.getColaborador() == colaborador)
            .mapToInt(contribucion -> contribucion.getHeladera().mesesActiva())
            .sum();
  }


  public void deleteTodas() {
    contribuciones.clear();
  }
}
