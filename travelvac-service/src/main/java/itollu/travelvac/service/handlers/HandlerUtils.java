package itollu.travelvac.service.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import io.undertow.util.PathTemplateMatch;

import java.io.IOException;
import java.util.Objects;

import static io.undertow.util.StatusCodes.UNAUTHORIZED;
import static io.undertow.util.StatusCodes.UNSUPPORTED_MEDIA_TYPE;

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

    static void ensureContentTypeJson(HttpServerExchange exchange) {
        var contentTypeHeader = exchange.getRequestHeaders().get(Headers.CONTENT_TYPE);

        if (Objects.isNull(contentTypeHeader) || contentTypeHeader.isEmpty()) {
            throw new ClientException(UNSUPPORTED_MEDIA_TYPE, "No Content-Type specified");
        }

        var contentType = contentTypeHeader.getFirst();

        if (!contentType.equals("application/json")) {
            var message = "Expected Content-Type 'application/json' but got '%s'".formatted(contentType);
            throw new ClientException(UNSUPPORTED_MEDIA_TYPE, message);
        }
    }

    static <T> T readJsonBody(Class<T> targetClass, HttpServerExchange exchange, JsonMapper json) throws IOException {
        ensureContentTypeJson(exchange);

        try (var bodyStream = exchange.getInputStream()) {
            return json.readValue(bodyStream, targetClass);
        }
    }

    static String requireRequestHeader(HttpString headerName, HttpServerExchange exchange) {
        var headerValues = exchange.getRequestHeaders().get(headerName);
        if (Objects.isNull(headerValues) || headerValues.isEmpty()) {
            var message = "%s header is required".formatted(headerName);
            throw new ClientException(UNAUTHORIZED, message);
        }

        return headerValues.getFirst();
    }

    static String requirePathParam(String paramName, HttpServerExchange exchange) {
        var pathMatch = exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY);
        if (Objects.isNull(pathMatch)) {
            throw new IllegalArgumentException("Expected PathTemplateMatch");
        }
        var pathParams = pathMatch.getParameters();
        var param = pathParams.get(paramName);
        if (Objects.isNull(param) || param.isEmpty()) {
            var message = "Expected path parameter [%s]".formatted(paramName);
            throw new IllegalArgumentException(message);
        }

        return param;
    }
}
