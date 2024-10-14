package ar.edu.utn.frba.dds.services.seeders;

import ar.edu.utn.frba.dds.models.entities.documentacion.Tarjeta;
import ar.edu.utn.frba.dds.models.repositories.documentacion.TarjetasRepository;

import java.util.List;
import java.util.UUID;

public class SeederTarjetas {
  public static void execute() {
    Tarjeta tarjeta1 = new Tarjeta(UUID.fromString("12345678-1234-1234-1234-123456789abc"));
    Tarjeta tarjeta2 = new Tarjeta(UUID.fromString("12345678-2345-2345-2345-123456789abc"));
    Tarjeta tarjeta3 = new Tarjeta(UUID.fromString("12345678-3456-3456-3456-123456789abc"));

    new TarjetasRepository().insertAll(List.of(tarjeta1, tarjeta2, tarjeta3));
  }
}
