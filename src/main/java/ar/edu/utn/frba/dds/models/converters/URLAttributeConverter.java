package ar.edu.utn.frba.dds.models.converters;

import javax.persistence.AttributeConverter;
import java.net.MalformedURLException;
import java.net.URL;

public class URLAttributeConverter implements AttributeConverter<URL, String> {
  @Override
  public String  convertToDatabaseColumn(URL url) {
    return url != null ? url.toString() : null;
  }

  @Override
  public URL convertToEntityAttribute(String dbData) {
    try {
      return dbData != null ? new URL(dbData) : null;
    } catch (MalformedURLException e) {
      throw new RuntimeException("Failed to convert String to URL: " + e.getMessage(), e);
    }
  }
}

