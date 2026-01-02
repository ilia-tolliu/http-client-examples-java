package itollu.travelvac.service.handlers;

import java.time.Instant;
import java.util.Map;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import static itollu.travelvac.service.handlers.HandlerUtils.applyContentTypeJson;

public class GetStatus implements HttpHandler {
  private final JsonMapper json;

  public GetStatus(JsonMapper json) {
    this.json = json;
  }

  @Override
  public void handleRequest(HttpServerExchange exchange) throws Exception {
    Map<String, Object> response = Map.of(
      "status", "OK",
      "hello", "World",
      "at", Instant.now()
    );

    String jsonResponse = json.writeValueAsString(response);

    applyContentTypeJson(exchange);
    exchange.getResponseSender().send(jsonResponse);
  }
}
