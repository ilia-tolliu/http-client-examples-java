package travelvac.service.http;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import travelvac.service.core.*;
import travelvac.service.ratelimiter.RateLimiter;

import java.time.LocalDateTime;
import java.util.List;

import static io.undertow.util.StatusCodes.BAD_REQUEST;
import static io.undertow.util.StatusCodes.CREATED;
import static travelvac.service.http.AuthUtils.authenticate;
import static travelvac.service.http.HandlerUtils.*;

public class PostBooking implements HttpHandler {

    private final BookingService bookingService;

    private final JsonMapper json;

    private final RateLimiter rateLimiter;

    public PostBooking(BookingService bookingService, JsonMapper json, RateLimiter rateLimiter) {
        this.json = json;
        this.bookingService = bookingService;
        this.rateLimiter = rateLimiter;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        var customerId = authenticate(exchange);
        rateLimiter.checkIn(customerId.format());
        var idempotencyKey = parseOrGenerateIdempotencyKey(exchange);
        var requestBody = readJsonBody(RequestBody.class, exchange, json);
        var newBooking = createNewBooking(customerId, requestBody);
        var booking = bookingService.bookAppointment(newBooking, idempotencyKey);
        var responseBody = BookingJson.fromBooking(booking);

        exchange.setStatusCode(CREATED);
        writeJsonBody(responseBody, exchange, json);
    }

    private NewBooking createNewBooking(CustomerId customerId, RequestBody requestBody) {
        try {
            return new NewBooking(
                    customerId,
                    requestBody.reference,
                    ClinicId.parse(requestBody.clinicId),
                    requestBody.scheduledAt,
                    requestBody.infections
            );
        } catch (IllegalDomainValue e) {
            throw new ClientException(BAD_REQUEST, e.getMessage());
        }
    }

    record RequestBody(
            String reference,
            String clinicId,
            LocalDateTime scheduledAt,
            List<String> infections
    ) {
    }
}
