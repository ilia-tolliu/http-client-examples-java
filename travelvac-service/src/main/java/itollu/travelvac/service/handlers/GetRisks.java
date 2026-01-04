package itollu.travelvac.service.handlers;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import itollu.travelvac.service.core.CountryCode;
import itollu.travelvac.service.core.IllegalDomainValue;
import itollu.travelvac.service.core.RiskService;

import java.util.Map;

import static io.undertow.util.StatusCodes.BAD_REQUEST;
import static itollu.travelvac.service.handlers.AuthUtils.authenticate;
import static itollu.travelvac.service.handlers.HandlerUtils.requirePathParam;
import static itollu.travelvac.service.handlers.HandlerUtils.writeJsonBody;

public class GetRisks implements HttpHandler {
    private static final String COUNTRY_PATH_PARAM = "country";

    private final RiskService riskService;

    private final JsonMapper json;

    public GetRisks(RiskService riskService, JsonMapper json) {
        this.riskService = riskService;
        this.json = json;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        var customerId = authenticate(exchange);
        var destination = parseDestination(exchange);
        var risks = riskService.getRisks(customerId, destination);
        var responseBody = Map.of(
                "risks", risks
        );
        writeJsonBody(responseBody, exchange, json);

    }

    private CountryCode parseDestination(HttpServerExchange exchange) {
        var destinationParam = requirePathParam(COUNTRY_PATH_PARAM, exchange);
        try {
            return new CountryCode(destinationParam);
        } catch (IllegalDomainValue e) {
            throw new ClientException(BAD_REQUEST, e.getMessage());
        }
    }
}
