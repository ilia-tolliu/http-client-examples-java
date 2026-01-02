package itollu.travelvac.service.app;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.undertow.Undertow;
import itollu.travelvac.service.handlers.Routes;

import static itollu.travelvac.service.handlers.Middleware.withLogging;

public class TravelvacApp {

  static void main() {
    System.out.println("Hello from Travelvac!");

    JsonMapper json = configureJsonMapper();
    Routes routes = new Routes(json);
    var router = routes.buildRouter();

    Undertow server = Undertow.builder()
      .addHttpListener(8080, "localhost")
      .setHandler(
        withLogging(
          router
        )
      )
      .build();

    server.start();
  }

  static JsonMapper configureJsonMapper() {
    return new JsonMapper().rebuild()
      .enable(SerializationFeature.INDENT_OUTPUT) // pretty print
      .build();
  }
}