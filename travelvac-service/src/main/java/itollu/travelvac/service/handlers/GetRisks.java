package itollu.travelvac.service.handlers;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import itollu.travelvac.service.core.RiskService;

import java.util.Map;

import static itollu.travelvac.service.handlers.AuthUtils.authenticate;
import static itollu.travelvac.service.handlers.HandlerUtils.*;

public class GetRisks implements HttpHandler {

    private final RiskService riskService;

    private final JsonMapper json;

    public GetRisks(RiskService riskService, JsonMapper json) {
        this.riskService = riskService;
        this.json = json;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        var customerId = authenticate(exchange);
        var country = parseCountryCode(exchange);
        var risks = riskService.getRisks(customerId, country);
        var responseBody = Map.of(
                "risks", risks
        );

        writeJsonBody(responseBody, exchange, json);
    }

}
