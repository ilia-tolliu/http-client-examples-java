package travelvac.service.handlers;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import travelvac.service.core.ClinicService;

import java.util.Map;

import static travelvac.service.handlers.AuthUtils.authenticate;
import static travelvac.service.handlers.HandlerUtils.parseCountryCode;
import static travelvac.service.handlers.HandlerUtils.writeJsonBody;

public class GetClinics implements HttpHandler {

    private final ClinicService clinicService;

    private final JsonMapper json;

    public GetClinics(ClinicService clinicService, JsonMapper json) {
        this.clinicService = clinicService;
        this.json = json;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        var customerId = authenticate(exchange);
        var country = parseCountryCode(exchange);
        var risks = clinicService.getClinics(customerId, country);
        var responseBody = Map.of(
                "clinics", risks
        );
        writeJsonBody(responseBody, exchange, json);

    }

}
