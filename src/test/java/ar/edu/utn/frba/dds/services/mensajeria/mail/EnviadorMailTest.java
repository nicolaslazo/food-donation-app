package ar.edu.utn.frba.dds.services.mensajeria.mail;

import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Properties;

class EnviadorMailTest {

  private MailServer mockMailServer;
  private TemplateEngine mockTemplateEngine;
  private EnviadorMail enviadorMail;
  private Session mockSession;

  @BeforeEach
  void setUp() {
    mockMailServer = mock(MailServer.class);
    mockTemplateEngine = mock(TemplateEngine.class);

    Properties mockProperties = new Properties();
    mockSession = Session.getInstance(mockProperties);

    when(mockMailServer.getSession()).thenReturn(mockSession);
    when(mockTemplateEngine.process(anyString(), any(Context.class))).thenReturn("Procesado HTML");

    enviadorMail = new EnviadorMail(mockMailServer);

    // Reemplazar el templateEngine estático
    EnviadorMail.templateEngine = mockTemplateEngine;
  }

  @Test
  void enviarMailExitoso() throws Exception {
    String destinatario = "prueba@gmail.com";
    String mensaje = "mensaje de prueba";

    // Usar un MockedStatic para interceptar la llamada a Transport.send
    try (MockedStatic<Transport> mockedTransport = mockStatic(Transport.class)) {
      enviadorMail.enviarMail(destinatario, mensaje);

      ArgumentCaptor<MimeMessage> messageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
      mockedTransport.verify(() -> Transport.send(messageCaptor.capture()));

      MimeMessage capturedMessage = messageCaptor.getValue();
      assertEquals("Nueva Notificación", capturedMessage.getSubject());
      assertEquals("prueba@gmail.com", capturedMessage.getAllRecipients()[0].toString());
      assertEquals("Procesado HTML", capturedMessage.getContent().toString());
    }
  }

  @Test
  void enviarMailFallido() {
    String destinatario = "prueba@gmail.com";
    String mensaje = "mensaje de prueba";

    when(mockTemplateEngine.process(anyString(), any(Context.class))).thenReturn(null);

    try (MockedStatic<Transport> mockedTransport = mockStatic(Transport.class)) {
      enviadorMail.enviarMail(destinatario, mensaje);

      mockedTransport.verify(() -> Transport.send(any(MimeMessage.class)), never());
    }
  }
}