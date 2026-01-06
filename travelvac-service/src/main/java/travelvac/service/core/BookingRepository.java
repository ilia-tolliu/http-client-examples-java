package travelvac.service.core;

import java.util.List;

public interface BookingRepository {

    Booking getBooking(CustomerId customerId, BookingId bookingId);

    List<Booking> getBookings(CustomerId customerId);

    IdempotencyMapping checkInIdempotencyKey(CustomerId customerId, IdempotencyKey idempotencyKey);

    void setIdempotentException(CustomerId customerId, IdempotencyKey idempotencyKey, RuntimeException e);

    Booking createBooking(NewBooking newBooking, IdempotencyKey idempotencyKey);
}
