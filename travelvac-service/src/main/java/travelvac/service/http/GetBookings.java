package travelvac.service.http;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import travelvac.service.core.BookingService;
import travelvac.service.ratelimiter.RateLimiter;

import java.util.Map;

import static travelvac.service.http.AuthUtils.authenticate;
import static travelvac.service.http.HandlerUtils.writeJsonBody;

public class GetBookings implements HttpHandler {

    private final BookingService bookingService;

    private final JsonMapper json;

    private final RateLimiter rateLimiter;

    public GetBookings(BookingService bookingService, JsonMapper json, RateLimiter rateLimiter) {
        this.bookingService = bookingService;
        this.json = json;
        this.rateLimiter = rateLimiter;
    }


    @Override
    public void handleRequest(HttpServerExchange exchange) {
        var customerId = authenticate(exchange);
        rateLimiter.checkIn(customerId.format());
        var bookings = bookingService.getBookings(customerId);
        var bookingJsons = bookings.stream()
                .map(BookingJson::fromBooking)
                .toList();
        var responseBody = Map.of(
                "bookings", bookingJsons
        );

        writeJsonBody(responseBody, exchange, json);
    }

}
