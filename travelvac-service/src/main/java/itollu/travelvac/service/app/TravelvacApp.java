package itollu.travelvac.service.app;

import io.undertow.Undertow;
import io.undertow.util.Headers;

import static io.undertow.Handlers.routing;

public class TravelvacApp {
    static void main() {
        System.out.println("Hello from Travelvac!");

        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(
                        routing()
                                .get(
                                        "status",
                                        exchange -> {
                                            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                                            exchange.getResponseSender().send("Hello World!");
                                        }
                                )
                )
                .build();

        server.start();
    }
}