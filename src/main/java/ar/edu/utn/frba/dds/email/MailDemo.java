package ar.edu.utn.frba.dds.email;

import ar.edu.utn.frba.dds.models.entities.contacto.Email;

import java.util.Scanner;

public class MailDemo {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    // Pedir al usuario que ingrese la dirección de correo electrónico
    System.out.println("Ingrese la dirección de correo electrónico del nuevo colaborador:");
    String email = scanner.nextLine();

    // Crear el contacto de email con la dirección ingresada por el usuario
    Email contactoEmail = new Email(email);

    // El envío de email se realiza en el constructor del colaborador
    System.out.println("Se ha enviado un email a: " + contactoEmail.getEmail());

    // Cerrar el scanner
    scanner.close();
  }
}
