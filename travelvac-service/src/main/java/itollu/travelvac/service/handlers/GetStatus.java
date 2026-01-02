package itollu.travelvac.service.handlers;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class GetStatus implements HttpHandler {

  @Override
  public void handleRequest(HttpServerExchange exchange) {
    exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
    exchange.getResponseSender().send("Hello World!");
  }
}
