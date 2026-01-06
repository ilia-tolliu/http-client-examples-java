package travelvac.service.core;

import java.time.Instant;

public record IdempotencyMapping(
        BookingId bookingId,
        RuntimeException exception,
        Instant createdAt
){
    public static IdempotencyMapping create() {
        return new IdempotencyMapping(null, null, Instant.now());
    }

    public IdempotencyMapping setBookingId(BookingId bookingId) {
        return new IdempotencyMapping(bookingId, this.exception, this.createdAt);
    }

    public IdempotencyMapping setException(RuntimeException exception) {
        return new IdempotencyMapping(this.bookingId, exception, this.createdAt);
    }
}
