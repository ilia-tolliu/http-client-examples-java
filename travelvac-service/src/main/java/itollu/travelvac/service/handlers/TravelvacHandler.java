package itollu.travelvac.service.handlers;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.BlockingHandler;
import itollu.travelvac.service.core.BookingService;
import itollu.travelvac.service.core.ClinicService;
import itollu.travelvac.service.core.RiskService;

import static io.undertow.Handlers.routing;
import static itollu.travelvac.service.handlers.Middleware.*;

public class TravelvacHandler implements HttpHandler {
    private final HttpHandler handler;

    public TravelvacHandler(
            JsonMapper json,
            RiskService riskService,
            ClinicService clinicService,
            BookingService bookingService
    ) {
        var router = routing()
                .get("status", new GetStatus(json))
                .get("countries/{countryCode}/risks", new GetRisks(riskService, json))
                .get("countries/{countryCode}/clinics", new GetClinics(clinicService, json))
                .post("bookings", new PostBooking(bookingService, json))
                .get("bookings/{bookingId}", new GetBooking(bookingService, json));

        handler = new BlockingHandler(
                withRequestId(
                        withExceptionHandler(
                                json,
                                withLogging(
                                        router
                                )
                        )
                )
        );
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        this.handler.handleRequest(exchange);
    }
}
