package travelvac.service.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.ExceptionHandler;

import static travelvac.service.http.HandlerUtils.applyContentTypeJson;

public class InternalServerErrorHandler implements HttpHandler {

  private static final Logger LOG = LoggerFactory.getLogger(InternalServerErrorHandler.class);

  @Override
  public void handleRequest(HttpServerExchange exchange) {
    // handleInternalServerException is a catch-all exception handler.
    // It should not throw any exception.
    // That's why JsonMapper is not used inside.

    var exception = exchange.getAttachment(ExceptionHandler.THROWABLE);
    LOG.error("Internal Server Error", exception);

    String response = """
      {
        "error": "Internal Server Error"
      }
      """;

    exchange.setStatusCode(500);
    applyContentTypeJson(exchange);
    exchange.getResponseSender().send(response);
  }
}
