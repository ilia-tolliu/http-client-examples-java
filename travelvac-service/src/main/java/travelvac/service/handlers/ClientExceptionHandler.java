package travelvac.service.handlers;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.ExceptionHandler;

import java.util.Map;

import static travelvac.service.handlers.HandlerUtils.writeJsonBody;

public class ClientExceptionHandler implements HttpHandler {

    private final JsonMapper json;

    public ClientExceptionHandler(JsonMapper json) {
        this.json = json;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        var exception = (ClientException) exchange.getAttachment(ExceptionHandler.THROWABLE);

        var responseBody = Map.of(
                "error", "Client Error",
                "message", exception.getClientMessage()
        );

        exchange.setStatusCode(exception.getStatusCode());
        writeJsonBody(responseBody, exchange, json);
    }
}
