package ar.edu.utn.frba.dds.controllers.home;

import io.javalin.http.Context;

public class HomeController {
  public void index(Context context) {
    System.out.println("Intento de renderizar");
    try {
      context.render("home/home.hbs");
    } catch (Exception e) {
      e.printStackTrace();
      context.status(500).result("Error: " + e.getMessage());
    }
  }
}
