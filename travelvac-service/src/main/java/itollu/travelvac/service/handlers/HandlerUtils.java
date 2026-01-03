package itollu.travelvac.service.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class HandlerUtils {

  private static final String APPLICATION_JSON = "application/json";

  static void applyContentTypeJson(HttpServerExchange exchange) {
    exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, APPLICATION_JSON);
  }

  static void writeJsonBody(Object body, HttpServerExchange exchange, JsonMapper json) {
    applyContentTypeJson(exchange);
    try {
      String jsonResponse = json.writeValueAsString(body);
      exchange.getResponseSender().send(jsonResponse);
    } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
    }
  }
}
