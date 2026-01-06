package travelvac.service.handlers;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import travelvac.service.core.BookingService;

import java.util.Map;

import static travelvac.service.handlers.AuthUtils.authenticate;
import static travelvac.service.handlers.HandlerUtils.writeJsonBody;

public class GetBookings implements HttpHandler {

    private final BookingService bookingService;

    private final JsonMapper json;

    public GetBookings(BookingService bookingService, JsonMapper json) {
        this.bookingService = bookingService;
        this.json = json;
    }


    @Override
    public void handleRequest(HttpServerExchange exchange) {
        var customerId = authenticate(exchange);
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
