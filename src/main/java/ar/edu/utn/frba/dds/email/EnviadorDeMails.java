package ar.edu.utn.frba.dds.email;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
public class EnviadorDeMails {
    // Configuración de correo electrónico
    private static final String userName = "ghrybalvarez@frba.utn.edu.ar";
    private static final String password = "ajnf cmhv ruam roaw";
    private static final String asunto = "Bienvenido";
    private static final String filePathPlantilla = "src/main/resources/email/plantilla-cuenta-nueva.html";

    public void enviarMail(String destinatario) {
      Properties props = new Properties(); //Propiedades para conectarse con el servidor SMTP de Gmail.
      props.put("mail.smtp.host", "smtp.gmail.com");
      props.put("mail.smtp.port", "587");
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true"); //Comunicación segura

      Session session = Session.getDefaultInstance(props, new jakarta.mail.Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(userName, password);
        }
      }); //Crea una sesion de correo electrónico usando las propiedades y auth da las credenciales

      try {
        MimeMessage message = new MimeMessage(session);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario, true));
        message.setSubject(asunto);
        // Leer la plantilla desde un archivo
        File file = new File(filePathPlantilla);
        StringBuilder htmlContent = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
          String line;
          while ((line = br.readLine()) != null) {
            htmlContent.append(line);
          }
        } catch (IOException e) {
          System.out.println("Error al leer el archivo: " + e.getMessage());
          return;
        }

        // Establecer el contenido HTML del mensaje previamente leido
        message.setContent(htmlContent.toString(), "text/html");
        System.out.println("Enviando...");
        Transport.send(message);
        System.out.println("Email enviado de manera exitosa.");

      } catch (MessagingException me) {
        System.out.println("Exception: " + me);
      }
    }
  }
