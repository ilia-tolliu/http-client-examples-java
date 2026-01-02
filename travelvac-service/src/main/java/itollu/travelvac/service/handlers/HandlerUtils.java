package itollu.travelvac.service.handlers;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class HandlerUtils {

  private static final String APPLICATION_JSON = "application/json";

  static void applyContentTypeJson(HttpServerExchange exchange) {
    exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, APPLICATION_JSON);
  }
}
