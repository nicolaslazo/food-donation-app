package ar.edu.utn.frba.dds.controllers.tienda;

import java.util.HashMap;
import java.util.Map;

import io.javalin.http.Context;

public class TiendaController {
  public void index(Context context) {
    context.render("tienda/home/tienda.hbs");
  }

  public void ofrecerProducto(Context context) {
    Map<String, Object> model = new HashMap<>();
    model.put("type", "Producto");
    context.render("tienda/ofrecerRecompensaEmpresa/ofrecerProductoServicio.hbs", model);
  }

  public void ofrecerServicio(Context context) {
    Map<String, Object> model = new HashMap<>();
    model.put("type", "Servicio");
    context.render("tienda/ofrecerRecompensaEmpresa/ofrecerProductoServicio.hbs", model);
  }

  public void canjearProductos(Context context) {
    Map<String, Object> model = new HashMap<>();
    model.put("type", "Producto");
    context.render("tienda/canjearPuntos/canjePuntosProductosServicios.hbs", model);
  }

  public void canjearServicios(Context context) {
    Map<String, Object> model = new HashMap<>();
    model.put("type", "Servicio");
    context.render("tienda/canjearPuntos/canjePuntosProductosServicios.hbs", model);
  }
}
