package travelvac.service.http;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import travelvac.service.core.BookingService;
import travelvac.service.ratelimiter.RateLimiter;

import java.util.Objects;

import static io.undertow.util.StatusCodes.NOT_FOUND;
import static travelvac.service.http.AuthUtils.authenticate;
import static travelvac.service.http.HandlerUtils.parseBookingId;
import static travelvac.service.http.HandlerUtils.writeJsonBody;

public class GetBooking implements HttpHandler {

    private final BookingService bookingService;

    private final JsonMapper json;

    private final RateLimiter rateLimiter;

    public GetBooking(BookingService bookingService, JsonMapper json, RateLimiter rateLimiter) {
        this.bookingService = bookingService;
        this.json = json;
        this.rateLimiter = rateLimiter;
    }


    @Override
    public void handleRequest(HttpServerExchange exchange) {
        var customerId = authenticate(exchange);
        rateLimiter.checkIn(customerId.format());
        var bookingId = parseBookingId(exchange);
        var booking = bookingService.getBooking(customerId, bookingId);
        if (Objects.isNull(booking)) {
            throw new ClientException(NOT_FOUND, "Booking not found");
        }

        var responseBody = BookingJson.fromBooking(booking);

        writeJsonBody(responseBody, exchange, json);
    }

}
