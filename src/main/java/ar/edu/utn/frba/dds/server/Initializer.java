package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.services.seeders.SeederHeladera;
import ar.edu.utn.frba.dds.services.seeders.SeederRoles;
import ar.edu.utn.frba.dds.services.seeders.SeederTarjetas;
import ar.edu.utn.frba.dds.services.seeders.SeederUsuarios;

public class Initializer {

  public static void init() {
    // Acá irían los datos para popular los repositorios
    new SeederRoles().seedRoles();
    new SeederUsuarios().seederUsuarios();
    new SeederHeladera().seederHeladera();
    SeederTarjetas.execute();
  }
}