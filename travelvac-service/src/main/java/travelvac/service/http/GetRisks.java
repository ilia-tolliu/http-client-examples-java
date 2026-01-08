package travelvac.service.http;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import travelvac.service.core.RiskService;
import travelvac.service.ratelimiter.RateLimiter;

import java.util.Map;

import static travelvac.service.http.AuthUtils.authenticate;
import static travelvac.service.http.HandlerUtils.*;

public class GetRisks implements HttpHandler {

    private final RiskService riskService;

    private final JsonMapper json;

    private final RateLimiter rateLimiter;

    public GetRisks(RiskService riskService, JsonMapper json, RateLimiter rateLimiter) {
        this.riskService = riskService;
        this.json = json;
        this.rateLimiter = rateLimiter;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        var customerId = authenticate(exchange);
        rateLimiter.checkIn(customerId.format());
        var country = parseCountryCode(exchange);
        var risks = riskService.getRisks(customerId, country);
        var responseBody = Map.of(
                "risks", risks
        );

        writeJsonBody(responseBody, exchange, json);
    }

}
