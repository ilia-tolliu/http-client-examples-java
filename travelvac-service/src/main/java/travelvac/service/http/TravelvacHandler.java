package travelvac.service.http;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.BlockingHandler;
import travelvac.service.core.BookingService;
import travelvac.service.core.ClinicService;
import travelvac.service.core.RiskService;
import travelvac.service.ratelimiter.RateLimiter;

import java.time.Duration;

import static io.undertow.Handlers.routing;
import static travelvac.service.http.Middleware.*;

public class TravelvacHandler implements HttpHandler {
    private final HttpHandler handler;

    public TravelvacHandler(
        JsonMapper json,
        RiskService riskService,
        ClinicService clinicService,
        BookingService bookingService
    ) {
        var rateLimiter = new RateLimiter(300, Duration.ofMinutes(1));

        var router = routing()
            .get("status", new GetStatus(json))
            .get("countries/{countryCode}/risks", new GetRisks(riskService, json, rateLimiter))
            .get("countries/{countryCode}/clinics", new GetClinics(clinicService, json, rateLimiter))
            .post("bookings", new PostBooking(bookingService, json, rateLimiter))
            .get("bookings", new GetBookings(bookingService, json, rateLimiter))
            .get("bookings/{bookingId}", new GetBooking(bookingService, json, rateLimiter));

        handler = new BlockingHandler(
            withRequestId(
                withGzipEncoding(
                    withExceptionHandler(
                        json,
                        withLogging(
                            router
                        )
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
