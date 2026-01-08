package travelvac.service.http;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import travelvac.service.core.ClinicService;
import travelvac.service.ratelimiter.RateLimiter;

import java.util.Map;

import static travelvac.service.http.AuthUtils.authenticate;
import static travelvac.service.http.HandlerUtils.parseCountryCode;
import static travelvac.service.http.HandlerUtils.writeJsonBody;

public class GetClinics implements HttpHandler {

    private final ClinicService clinicService;

    private final JsonMapper json;

    private final RateLimiter rateLimiter;

    public GetClinics(ClinicService clinicService, JsonMapper json, RateLimiter rateLimiter) {
        this.clinicService = clinicService;
        this.json = json;
        this.rateLimiter = rateLimiter;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        var customerId = authenticate(exchange);
        rateLimiter.checkIn(customerId.format());
        var country = parseCountryCode(exchange);
        var risks = clinicService.getClinics(customerId, country);
        var responseBody = Map.of(
                "clinics", risks
        );
        writeJsonBody(responseBody, exchange, json);

    }

}
