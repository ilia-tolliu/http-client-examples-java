package itollu.travelvac.service.handlers;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import itollu.travelvac.service.core.BookingService;

import static itollu.travelvac.service.handlers.AuthUtils.authenticate;
import static itollu.travelvac.service.handlers.HandlerUtils.parseBookingId;
import static itollu.travelvac.service.handlers.HandlerUtils.writeJsonBody;

public class GetBooking implements HttpHandler {

    private final BookingService bookingService;

    private final JsonMapper json;

    public GetBooking(BookingService bookingService, JsonMapper json) {
        this.bookingService = bookingService;
        this.json = json;
    }


    @Override
    public void handleRequest(HttpServerExchange exchange) {
        var customerId = authenticate(exchange);
        var bookingId = parseBookingId(exchange);
        var booking = bookingService.getBooking(customerId, bookingId);
        var responseBody = BookingJson.fromBooking(booking);

        writeJsonBody(responseBody, exchange, json);
    }

}
