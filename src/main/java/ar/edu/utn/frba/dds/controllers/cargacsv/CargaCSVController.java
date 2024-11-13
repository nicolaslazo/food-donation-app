package ar.edu.utn.frba.dds.controllers.cargacsv;

import ar.edu.utn.frba.dds.migrations.CargadorMasivoDeContribuciones;
import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.Contacto;
import ar.edu.utn.frba.dds.models.entities.contacto.MensajeAContactoException;
import ar.edu.utn.frba.dds.models.entities.contribucion.Contribucion;
import ar.edu.utn.frba.dds.models.repositories.colaborador.ColaboradorRepository;
import ar.edu.utn.frba.dds.models.repositories.contacto.ContactosRepository;
import ar.edu.utn.frba.dds.models.repositories.contribucion.ContribucionRepository;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CargaCSVController {
  final String plantillaMailBienvenida = """
      Usted ha sido incorporad@ al nuevo servicio de donaciones de viandas.

      Puede entrar al sistema con las siguientes credenciales temporarias: '%s'.""";

  public void index(Context context) {
    Map<String, Object> model = context.attribute("model");

    context.render("cargacsv/cargacsv.hbs", model);
  }

  public void create(Context context) {
    try {
      UploadedFile uploadedFile = context.uploadedFile("archivo");

      if (uploadedFile == null) {
        context.json(new HashMap<String, Object>() {{
          put("message", "No se encontró ningún archivo");
          put("success", false);
        }});
        return;
      }

      Path tempFile = Files.createTempFile("upload-", ".csv");
      Files.write(tempFile, uploadedFile.content().readAllBytes());

      CargadorMasivoDeContribuciones cargador = new CargadorMasivoDeContribuciones(tempFile);

      List<Contribucion> contribuciones = new ArrayList<>();
      while (cargador.hasNext()) {
        Contribucion contribucion = cargador.next();
        contribuciones.add(contribucion);
      }

      Files.delete(tempFile);

      Set<Colaborador> colaboradores = cargador.getColaboradores();
      Set<Contacto> contactos = cargador.getContactos();

      new ColaboradorRepository().insertAll(colaboradores);
      new ContactosRepository().insertAll(contactos);
      new ContribucionRepository().insertAll(contribuciones);

      for (Colaborador colaborador : colaboradores) {
        String contrasenaPlaintextTemporaria = colaborador.getUsuario().getContrasenaPlaintextTemporaria();

        new ContactosRepository()
            .get(colaborador.getUsuario())
            .forEach(contacto -> {
              try {
                contacto.enviarMensaje(plantillaMailBienvenida.formatted(contrasenaPlaintextTemporaria));
              } catch (MensajeAContactoException e) {
                throw new RuntimeException(e);
              }
            });
      }

      context.json(new HashMap<String, Object>() {{
        put("message", "Se procesaron %d contribuciones para %d colaboradores".formatted(contribuciones.size(), colaboradores.size()));
        put("success", true);
      }});
    } catch (Exception e) {
      e.printStackTrace();
      context.json(new HashMap<String, Object>() {{
        put("message", "Error al procesar el archivo: " + e.getMessage());
        put("success", false);
      }});
    }
  }
}

