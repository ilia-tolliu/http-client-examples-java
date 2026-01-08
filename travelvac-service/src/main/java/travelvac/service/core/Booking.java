package travelvac.service.core;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public record Booking(
        BookingId bookingId,
        CustomerId customerId,
        String reference,
        ClinicId clinicId,
        List<String> infections,
        LocalDateTime scheduledAt,
        Instant createdAt
) {
}
