package ar.edu.utn.frba.dds.controllers.documentacion;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.entities.users.PermisoDenegadoException;
import ar.edu.utn.frba.dds.models.entities.users.Usuario;
import ar.edu.utn.frba.dds.models.repositories.RepositoryException;
import ar.edu.utn.frba.dds.models.repositories.documentacion.TarjetasRepository;
import lombok.NonNull;

import java.time.ZonedDateTime;
import java.util.UUID;

public class TarjetaController {

 static final TarjetasRepository tarjetasRepository = new TarjetasRepository();

  public static Tarjeta crear(@NonNull UUID uuid, @NonNull Usuario creador) throws RepositoryException, PermisoDenegadoException {
    creador.assertTienePermiso("crearTarjetas", "Sólo administradores pueden crear tarjetas");

    Tarjeta tarjetaNueva = new Tarjeta(uuid);

    tarjetasRepository.insert(tarjetaNueva);

    return tarjetaNueva;
  }

  public static void darDeAlta(@NonNull Tarjeta tarjeta, @NonNull Usuario recipiente, @NonNull Colaborador proveedor)
      throws PermisoDenegadoException {
    proveedor
        .getUsuario()
        .assertTienePermiso("asignarTarjetas", "Sólo colaboradores pueden entregar tarjetas");

    if (tarjetasRepository.getVigentePara(recipiente).isPresent())
      throw new PermisoDenegadoException("Este usuario ya tiene una tarjeta asignada");

    tarjeta.setEnAlta(recipiente, proveedor, ZonedDateTime.now());
  }

  public static void darDeBaja(@NonNull Tarjeta tarjeta, @NonNull Usuario responsable) throws PermisoDenegadoException {
    responsable
        .assertTienePermiso("darBajaTarjetas", "Sólo administradores pueden dar tarjetas de baja");

    tarjeta.setDeBaja(responsable, ZonedDateTime.now());
  }
}
