package itollu.travelvac.service.app;

import io.undertow.Undertow;

import static itollu.travelvac.service.handlers.Handlers.withLogging;
import static itollu.travelvac.service.handlers.Routes.buildRouter;

public class TravelvacApp {

  static void main() {
    System.out.println("Hello from Travelvac!");

    Undertow server = Undertow.builder()
      .addHttpListener(8080, "localhost")
      .setHandler(
        withLogging(
          buildRouter()
        )
      )
      .build();

    server.start();
  }
}