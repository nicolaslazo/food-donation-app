package ar.edu.utn.frba.dds.services.mensajeria.mail;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.mail.Session;
public class ServerMailTest {
  private MailServer mailServer;
  @BeforeEach
  public void setUp() {
    mailServer = new MailServer();
  }
  @Test
  public void testMailServerInitialization() {
    Session session = mailServer.getSession();
    assertNotNull(session, "La sesión de correo no debería ser nula.");
  }
}