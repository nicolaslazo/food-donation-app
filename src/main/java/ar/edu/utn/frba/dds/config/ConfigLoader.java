package ar.edu.utn.frba.dds.config;

import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
  private static ConfigLoader instancia = null;
  private final Properties properties = new Properties();

  private ConfigLoader() {
    try {
      try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
        properties.load(input);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static ConfigLoader getInstancia() {
    if (instancia == null) {
      instancia = new ConfigLoader();
    }

    return instancia;
  }

  public String getProperty(@NonNull String key) {
    return properties.getProperty(key);
  }
}
