package itollu.travelvac.service.handlers;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import itollu.travelvac.service.core.ClinicService;
import itollu.travelvac.service.core.CountryCode;
import itollu.travelvac.service.core.IllegalDomainValue;

import java.util.Map;

import static io.undertow.util.StatusCodes.BAD_REQUEST;
import static itollu.travelvac.service.handlers.AuthUtils.authenticate;
import static itollu.travelvac.service.handlers.HandlerUtils.requirePathParam;
import static itollu.travelvac.service.handlers.HandlerUtils.writeJsonBody;

public class GetClinics implements HttpHandler {
    private static final String COUNTRY_PATH_PARAM = "country";

    private final ClinicService clinicService;

    private final JsonMapper json;

    public GetClinics(ClinicService clinicService, JsonMapper json) {
        this.clinicService = clinicService;
        this.json = json;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        var customerId = authenticate(exchange);
        var destination = parseDestination(exchange);
        var risks = clinicService.getClinics(customerId, destination);
        var responseBody = Map.of(
                "clinics", risks
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
