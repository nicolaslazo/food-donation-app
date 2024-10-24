package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.services.seeders.SeederHeladera;
import ar.edu.utn.frba.dds.services.seeders.SeederRoles;
import ar.edu.utn.frba.dds.services.seeders.SeederUsuarios;
import ar.edu.utn.frba.dds.services.seeders.SeederViandas;
import lombok.SneakyThrows;

public class Initializer {
  @SneakyThrows
  public static void init() {
    // Acá irían los datos para popular los repositorios
    new SeederRoles().seedRoles();
    SeederUsuarios.execute();
    new SeederHeladera().seederHeladera();
    SeederViandas.execute();
  }
}