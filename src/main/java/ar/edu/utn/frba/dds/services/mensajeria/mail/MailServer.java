package ar.edu.utn.frba.dds.services.mensajeria.mail;

import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

//Configuracion del servidor
public class MailServer {
  private static final String userName = "ghrybalvarez@frba.utn.edu.ar";
  private static final String password = "vfew lkmx wmhv nhwj";

  private Properties mailProperties;
  @Getter
  private Session session;

  public MailServer() {
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
      inicializarMailProperties();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  private void inicializarMailProperties() {
    mailProperties.put("mail.smtp.host", mailProperties.getProperty("mail.smtp.host"));
    mailProperties.put("mail.smtp.port", mailProperties.getProperty("mail.smtp.port"));
    mailProperties.put("mail.smtp.auth", mailProperties.getProperty("mail.smtp.auth"));
    mailProperties.put("mail.smtp.starttls.enable", mailProperties.getProperty("mail.smtp.starttls.enable"));

    session = Session.getDefaultInstance(mailProperties, new jakarta.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
      }
    });
  }

}