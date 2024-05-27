package ar.edu.utn.frba.dds.email;


import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class EnviadorDeMails {
  // Configuración de correo electrónico
  private static final String userName = "xxxxx@gmail.com";
  private static final String password = "xxxxxxxxxxxx";
  private static final String asunto = "Bienvenido";
  private static final String filePathPlantilla = "src/main/resources/email/plantilla-cuenta-nueva.html";

  private Properties mailProperties;

  public EnviadorDeMails() {
    inicializarCorreo();
  }

  private void inicializarCorreo() {
    mailProperties = new Properties();
    try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
      if (input == null) {
        System.out.println("Lo sentimos, no se pudo encontrar el archivo de configuración.");
        return;
      }
      mailProperties.load(input);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  private void inicializarMailProperties() {
    mailProperties.put("mail.smtp.host", mailProperties.getProperty("mail.smtp.host"));
    mailProperties.put("mail.smtp.port", mailProperties.getProperty("mail.smtp.port"));
    mailProperties.put("mail.smtp.auth", mailProperties.getProperty("mail.smtp.auth"));
    mailProperties.put("mail.smtp.starttls.enable", mailProperties.getProperty("mail.smtp.starttls.enable"));
  }

  public void enviarMail(String destinatario) {
    Session session = Session.getDefaultInstance(mailProperties, new jakarta.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
      }
    });

    try {
      MimeMessage message = new MimeMessage(session);
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario, true));
      message.setSubject(asunto);
      String htmlContent;
      try {
        htmlContent = Files.readString(Paths.get(filePathPlantilla));
      } catch (IOException e) {
        System.out.println("Error al leer el archivo: " + e.getMessage());
        return;
      }

      message.setContent(htmlContent, "text/html");
      System.out.println("Enviando...");
      Transport.send(message);
      System.out.println("Email enviado de manera exitosa.");

    } catch (MessagingException me) {
      System.out.println("Exception: " + me);
    }
  }
}
