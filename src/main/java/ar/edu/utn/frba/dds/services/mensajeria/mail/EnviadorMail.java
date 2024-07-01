package ar.edu.utn.frba.dds.services.mensajeria.mail;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

//El que posta envia el mail
public class EnviadorMail {

  private static EnviadorMail instancia = null;

  public static EnviadorMail getInstancia()  {
    if (instancia == null) {
      instancia = new EnviadorMail(new MailServer());
    }
    return instancia;
  }

  private final MailServer mailServer;
  private static final String asunto = "Nueva Notificación";
  private static final String templateName = "email/plantilla-mail";
  public static TemplateEngine templateEngine = TemplateEngineConfig.createTemplateEngine();

  public EnviadorMail(MailServer mailServer) {
    this.mailServer = mailServer;
    templateEngine = TemplateEngineConfig.createTemplateEngine();
  }

  private String procesarPlantillaHTML(String mensaje) {
    Context context = new Context();
    context.setVariable("mensaje", mensaje);
    return templateEngine.process(templateName, context);
  }

  public void enviarMail(String destinatario, String mensaje) {
    String body = procesarPlantillaHTML(mensaje);
    if (body == null) {
      System.out.println("Error al procesar la plantilla HTML.");
      return;
    }

    Session session = mailServer.getSession();

    try {
      MimeMessage message = new MimeMessage(session);
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario, true));
      message.setSubject(asunto);
      message.setContent(body, "text/html");

      System.out.println("Enviando...");
      jakarta.mail.Transport.send(message); // Envío del mensaje

      System.out.println("Email enviado de manera exitosa.");
    } catch (MessagingException me) {
      System.out.println("Exception: " + me);
    }


  }

  public static void main(String[] args) {
    EnviadorMail.getInstancia().enviarMail("guadaahryb@gmail.com", "aaaaaaaaa");
  }
}
