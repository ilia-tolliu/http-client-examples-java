package itollu.travelvac.service.app;

import java.util.TimeZone;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.undertow.Undertow;
import itollu.travelvac.service.handlers.Routes;
import static itollu.travelvac.service.handlers.Middleware.withExceptionHandler;
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
        withExceptionHandler(
          json,
          withLogging(
            router
          )
        )
      )
      .build();

    server.start();
  }

  static JsonMapper configureJsonMapper() {
    var dateFormat = new StdDateFormat()
      .withTimeZone(TimeZone.getTimeZone("UTC"));

    return new JsonMapper().rebuild()
      .enable(SerializationFeature.INDENT_OUTPUT) // pretty print
      .addModule(new JavaTimeModule())
      .defaultDateFormat(dateFormat)
      .build();
  }
}