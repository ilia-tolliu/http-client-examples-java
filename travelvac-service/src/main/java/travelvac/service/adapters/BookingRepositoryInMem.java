package travelvac.service.adapters;

import travelvac.service.core.*;
import org.jetbrains.annotations.Contract;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class BookingRepositoryInMem implements BookingRepository {

    private final ConcurrentHashMap<BookingKey, Booking> bookings = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<IdempotencyMappingKey, IdempotencyMapping> idempotencyMappings = new ConcurrentHashMap<>();

    @Override
    public Booking getBooking(CustomerId customerId, BookingId bookingId) {
        var key = new BookingKey(customerId, bookingId);
        return bookings.get(key);
    }

    @Override
    public List<Booking> getBookings(CustomerId customerId) {
        return bookings.values()
                .stream()
                .filter(booking -> booking.customerId().equals(customerId))
                .toList();
    }

    @Override
    public IdempotencyMapping checkInIdempotencyKey(CustomerId customerId, IdempotencyKey idempotencyKey) {
        var key = new IdempotencyMappingKey(customerId, idempotencyKey);
        var newRecord = IdempotencyMapping.create();
        return idempotencyMappings.putIfAbsent(key, newRecord);
    }

    public void setIdempotentException(CustomerId customerId, IdempotencyKey idempotencyKey, RuntimeException e) {
        var key = new IdempotencyMappingKey(customerId, idempotencyKey);
        idempotencyMappings.compute(key, (_, idempotencyMapping) -> {
            validateIdempotencyMappingUpdatable(idempotencyMapping);
            return idempotencyMapping.setException(e);

        });
    }

    @Override
    public Booking createBooking(NewBooking newBooking, IdempotencyKey idempotencyKey) {
        var idempotencyMappingKey = new IdempotencyMappingKey(newBooking.customerId(), idempotencyKey);
        var bookingId = BookingId.generate();
        var bookingKey = new BookingKey(newBooking.customerId(), bookingId);

        idempotencyMappings.compute(idempotencyMappingKey, (_, idempotencyMapping) -> {
            validateIdempotencyMappingUpdatable(idempotencyMapping);

            var booking = new Booking(
                    bookingId,
                    newBooking.customerId(),
                    newBooking.reference(),
                    newBooking.clinicId(),
                    newBooking.infections(),
                    newBooking.scheduledAt(),
                    Instant.now()
            );
            bookings.put(bookingKey, booking);

            return idempotencyMapping.setBookingId(bookingId);
        });

        return bookings.get(bookingKey);
    }

    @Contract("null -> fail")
    private void validateIdempotencyMappingUpdatable(IdempotencyMapping idempotencyMapping) {
        if (Objects.isNull(idempotencyMapping)) {
            throw new RuntimeException("Expected existing idempotency mapping");
        }
        if (Objects.nonNull(idempotencyMapping.bookingId())) {
            throw new RuntimeException("Expected no bookingId already set");
        }
        if (Objects.nonNull(idempotencyMapping.exception())) {
            throw new RuntimeException("Expected no exception already set");
        }
    }

    private record BookingKey(
            CustomerId customerId,
            BookingId bookingId
    ) {
    }

    private record IdempotencyMappingKey(
            CustomerId customerId,
            IdempotencyKey idempotencyKey
    ) {
    }
}
