package itollu.travelvac.service.handlers;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import itollu.travelvac.service.core.*;

import java.time.LocalDateTime;
import java.util.List;

import static io.undertow.util.StatusCodes.BAD_REQUEST;
import static io.undertow.util.StatusCodes.CREATED;
import static itollu.travelvac.service.handlers.AuthUtils.authenticate;
import static itollu.travelvac.service.handlers.HandlerUtils.*;

public class PostBooking implements HttpHandler {

    private final JsonMapper json;

    private final BookingService bookingService;

    public PostBooking(BookingService bookingService, JsonMapper json) {
        this.json = json;
        this.bookingService = bookingService;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        var customerId = authenticate(exchange);
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
