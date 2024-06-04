package ar.edu.utn.frba.dds.auth;

import org.apache.commons.text.RandomStringGenerator;

public class GeneradorDeContrasenias {

  public static String generarContrasenia() {
    int length = 12;

    RandomStringGenerator generadorContra = new RandomStringGenerator.Builder()
        .withinRange('a', 'z')
        .filteredBy(Character::isLetter)
        .build();

    return generadorContra.generate(length);
  }
}


