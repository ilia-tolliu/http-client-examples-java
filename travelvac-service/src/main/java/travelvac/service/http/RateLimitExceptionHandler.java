package travelvac.service.http;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.StatusCodes;

public class RateLimitExceptionHandler implements HttpHandler {

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        exchange.setStatusCode(StatusCodes.TOO_MANY_REQUESTS);
        exchange.getResponseSender().close();
    }
}
