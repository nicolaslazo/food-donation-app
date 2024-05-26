package ar.edu.utn.frba.dds.auth;

import org.apache.commons.text.RandomStringGenerator;

public class GeneradorDeContrasenias {

    public String generarContrasenias() {
      int length = 12;

      RandomStringGenerator generadorMayusculas = new RandomStringGenerator.Builder()
          .withinRange('A', 'Z')
          .filteredBy(Character::isLetter)
          .build();

      RandomStringGenerator generadorMinusculas = new RandomStringGenerator.Builder()
          .withinRange('a', 'z')
          .filteredBy(Character::isLetter)
          .build();

      String mayusculas = generadorMayusculas.generate(length /2);
      String minusculas = generadorMinusculas.generate(length /2);

      return mayusculas + minusculas;
    }

}
