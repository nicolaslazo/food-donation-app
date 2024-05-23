package ar.edu.utn.frba.dds.config;

import lombok.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
  private final Properties properties;

  public ConfigLoader(String nombreDeArchivo) throws IOException {
    properties = new Properties();
    cargarProperties(nombreDeArchivo);
  }

  private void cargarProperties(String nombreDeArchivo) throws IOException {
    @NonNull
    InputStream input = getClass().getClassLoader().getResourceAsStream(nombreDeArchivo);

    properties.load(input);
  }

  public String getProperty(@NonNull String key) {
    return properties.getProperty(key);
  }
}
