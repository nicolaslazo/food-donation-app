package ar.edu.utn.frba.dds.email;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class EnviadorDeMails {
  // Configuración de correo electrónico
  private static final String userName = "ghrybalvarez@frba.utn.edu.ar";
  private static final String password = "vfew lkmx wmhv nhwj";
  private static final String asunto = "Bienvenido";
  private static final String templateName = "email/plantilla-cuenta-nueva"; // No leading slash


  private Properties mailProperties;
  private TemplateEngine templateEngine;

  public EnviadorDeMails() {
    inicializarCorreo();
    inicializarTemplateEngine();
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
  }

  private void inicializarTemplateEngine() {
    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setTemplateMode("HTML");
    templateResolver.setPrefix("templates/"); // Ensure correct path
    templateResolver.setSuffix(".html");
    templateResolver.setCharacterEncoding("UTF-8");
    templateEngine = new TemplateEngine();
    templateEngine.setTemplateResolver(templateResolver);
  }

  private String procesarPlantillaHTML(String email, String contrasenaTemporaria) {
    Context context = new Context();
    context.setVariable("email", email);
    context.setVariable("contrasena_temporaria", contrasenaTemporaria);
    return templateEngine.process(templateName, context);
  }

  public void enviarMail(String destinatario, String contrasenaTemporaria) {
    String htmlContent = procesarPlantillaHTML(destinatario, contrasenaTemporaria);
    if (htmlContent == null) {
      System.out.println("Error al procesar la plantilla HTML.");
      return;
    }

    Session session = Session.getDefaultInstance(mailProperties, new jakarta.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
      }
    });

    try {
      MimeMessage message = new MimeMessage(session);
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario, true));
      message.setSubject(asunto);
      message.setContent(htmlContent, "text/html");

      System.out.println("Enviando...");
      Transport.send(message);
      System.out.println("Email enviado de manera exitosa.");
    } catch (MessagingException me) {
      System.out.println("Exception: " + me);
    }
  }
}

