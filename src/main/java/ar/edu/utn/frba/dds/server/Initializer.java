package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.services.seeders.SeederRoles;

public class Initializer {

  public static void init() {
    // Acá irían los datos para popular los repositorios
    new SeederRoles().seedRoles();
  }
}