package ar.edu.utn.frba.dds.email;

import ar.edu.utn.frba.dds.models.entities.colaborador.Colaborador;
import ar.edu.utn.frba.dds.models.entities.contacto.ContactoEmail;
import ar.edu.utn.frba.dds.models.entities.documentacion.Documento;
import ar.edu.utn.frba.dds.models.entities.documentacion.TipoDocumento;
import java.util.Scanner;

public class MailDemo {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    // Pedir al usuario que ingrese la dirección de correo electrónico
    System.out.println("Ingrese la dirección de correo electrónico del nuevo colaborador:");
    String email = scanner.nextLine();

    // Crear el documento del colaborador
    Documento documento = new Documento(TipoDocumento.fromString("DNI"), 12345678);

    // Crear el contacto de email con la dirección ingresada por el usuario
    ContactoEmail contactoEmail = new ContactoEmail(email);

    // Crear el colaborador
    Colaborador colaborador = new Colaborador(documento, "Persona", "Ejemplo", contactoEmail);

    // El envío de email se realiza en el constructor del colaborador
    System.out.println("Se ha enviado un email a: " + contactoEmail.getEmail());

    // Cerrar el scanner
    scanner.close();
  }
}
