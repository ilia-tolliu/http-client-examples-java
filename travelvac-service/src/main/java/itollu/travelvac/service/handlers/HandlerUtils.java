package itollu.travelvac.service.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import io.undertow.util.PathTemplateMatch;
import itollu.travelvac.service.core.CountryCode;
import itollu.travelvac.service.core.IllegalDomainValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

import static io.undertow.util.StatusCodes.*;

public class HandlerUtils {

    private final static Logger LOG = LoggerFactory.getLogger(HandlerUtils.class);

    private static final String APPLICATION_JSON = "application/json";

    private static final String COUNTRY_PATH_PARAM = "countryCode";


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
        var contentTypeMain = contentType.split(";")[0];

        if (!contentTypeMain.equals("application/json")) {
            var message = "Expected Content-Type 'application/json' but got '%s'".formatted(contentType);
            throw new ClientException(UNSUPPORTED_MEDIA_TYPE, message);
        }
    }

    static <T> T readJsonBody(Class<T> targetClass, HttpServerExchange exchange, JsonMapper json) {
        ensureContentTypeJson(exchange);

        try (
                var inputStream = exchange.getInputStream()
        ) {
            return json.readValue(inputStream, targetClass);
        } catch (IOException e) {
            // TODO add request id to log
            LOG.error("Failed to read request body", e);
            var message = "Failed to read request body: %s".formatted(e.getMessage());
            throw new ClientException(BAD_REQUEST, message);
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

    static CountryCode parseCountry(HttpServerExchange exchange) {
        var countryParam = requirePathParam(COUNTRY_PATH_PARAM, exchange);
        try {
            return new CountryCode(countryParam);
        } catch (IllegalDomainValue e) {
            throw new ClientException(BAD_REQUEST, e.getMessage());
        }
    }
}
