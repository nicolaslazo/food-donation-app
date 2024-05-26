package ar.edu.utn.frba.dds.config;

import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
  private final Properties properties;

  public ConfigLoader(String nombreDeArchivo) {
    properties = new Properties();
    try {
      cargarProperties(nombreDeArchivo);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void cargarProperties(String nombreDeArchivo) throws IOException {
    try (InputStream input = getClass().getClassLoader().getResourceAsStream(nombreDeArchivo)) {
      properties.load(input);
    }
  }

  public String getProperty(@NonNull String key) {
    return properties.getProperty(key);
  }
}
