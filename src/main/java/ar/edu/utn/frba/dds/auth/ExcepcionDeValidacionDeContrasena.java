package ar.edu.utn.frba.dds.auth;

/**
 * Error a tirar en el caso que una contraseña no sea válida.
 */
public class ExcepcionDeValidacionDeContrasena extends Exception {
  public ExcepcionDeValidacionDeContrasena(String mensaje) {
    super(mensaje);
  }
}
