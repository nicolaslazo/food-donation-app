package ar.edu.utn.frba.dds.controllers.image;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ImageController {
  public String guardarImagen(Context context, String imgInputName) {
    String pathImagen = null;

    // Define el directorio de destino fuera del source code
    Path uploadsDir = Paths.get("src/main/resources/public/uploads");
    if (!Files.exists(uploadsDir)) {
      try {
        Files.createDirectories(uploadsDir); // Crear el directorio si no existe
      } catch (IOException e) {
        throw new RuntimeException("Error al crear el directorio para las imágenes", e);
      }
    }

    try {
      UploadedFile file = context.uploadedFile(imgInputName);

      if (file != null) {
        // Asegura que el archivo se guarde con un nombre único si es necesario
        String uniqueFilename = System.currentTimeMillis() + "_" + file.filename();
        Path destino = uploadsDir.resolve(uniqueFilename);

        try (InputStream inputStream = file.content()) {
          Files.copy(inputStream, destino, StandardCopyOption.REPLACE_EXISTING);
        }

        // Generar el path relativo para usar en la vista
        pathImagen = "/public/uploads/" + uniqueFilename;
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("Error al guardar la imagen", e);
    }

    return pathImagen; // Retorna la ruta de la imagen guardada
  }
}
