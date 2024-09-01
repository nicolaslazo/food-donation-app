package ar.edu.utn.frba.dds.models.repositories.documentacion;


import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TarjetasRepository implements ITarjetasRepository {
  static TarjetasRepository instancia = null;
  List<Tarjeta> tarjetas;

  TarjetasRepository() {
    tarjetas = new ArrayList<>();
  }

  public static TarjetasRepository getInstancia() {
    if (instancia == null) instancia = new TarjetasRepository();

    return instancia;
  }

  public Optional<Tarjeta> get(UUID id) {
    return tarjetas.stream().filter(ta -> ta.getId().equals(id)).findFirst();
  }

  public Optional<Tarjeta> getVigentePara(Usuario usuario) {
    return tarjetas
        .stream()
        .filter(tarjeta -> tarjeta.getRecipiente() == usuario &&
            tarjeta.getFechaAlta() != null &&
            tarjeta.getFechaBaja() == null)
        .findFirst();
  }

  public UUID insert(Tarjeta tarjeta) throws RepositoryException {
    if (get(tarjeta.getId()).isPresent()) {
      throw new RepositoryException("Otra tarjeta con este identificador ya existe");
    }

    tarjetas.add(tarjeta);

    return tarjeta.getId();
  }

  public void deleteTodas() {
    tarjetas.clear();
  }
}
