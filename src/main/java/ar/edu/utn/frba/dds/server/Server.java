package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.config.ConfigLoader;
import ar.edu.utn.frba.dds.server.handlers.AppHandlers;
import ar.edu.utn.frba.dds.server.middleware.PermisosMiddleware;
import com.github.jknack.handlebars.Handlebars;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.HttpStatus;

import java.io.IOException;
import java.util.function.Consumer;

public class Server {
  private static Javalin app = null;

  public static Javalin app() {
    if (app == null)
      throw new RuntimeException("App no inicializada");
    return app;
  }

  public static void init() {
    if (app == null) {
      int port = Integer.parseInt(ConfigLoader.getInstancia().getProperty("server_port"));
      app = Javalin.create(config()).start(port);

      if (Boolean.parseBoolean(ConfigLoader.getInstancia().getProperty("dev_mode"))) {
        Initializer.init();
      }

      PermisosMiddleware.apply(app);
      AppHandlers.applyHandlers(app);
      Router.init(app);
    }
  }

  private static Consumer<JavalinConfig> config() {
    return config -> {
      config.staticFiles.add(staticFiles -> {
        staticFiles.hostedPath = "/public";
        staticFiles.directory = "/public";
      });

      config.fileRenderer(new JavalinRenderer().register("hbs", (path, model, context) -> {
        Handlebars handlebars = new Handlebars();
        try {
          return handlebars.compile("templates/" + path.replace(".hbs", "")).apply(model);
        } catch (IOException e) {
          e.printStackTrace();
          context.status(HttpStatus.NOT_FOUND);
          return "No se encuentra la página indicada...";
        }
      }));
    };
  }
}