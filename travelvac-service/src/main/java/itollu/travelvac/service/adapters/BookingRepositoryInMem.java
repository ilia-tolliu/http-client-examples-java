package itollu.travelvac.service.adapters;

import itollu.travelvac.service.core.*;

import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class BookingRepositoryInMem implements BookingRepository {
    private final ConcurrentHashMap<Key, Booking> bookings = new ConcurrentHashMap<>();

    @Override
    public Booking getBooking(CustomerId customerId, BookingId bookingId) {
        var key = new Key(customerId, bookingId);
        return bookings.get(key);
    }

    @Override
    public Booking createBooking(NewBooking newBooking) {
        var bookingId = BookingId.generate();
        var key = new Key(newBooking.customerId(), bookingId);
        var booking = new Booking(
                bookingId,
                newBooking.customerId(),
                newBooking.reference(),
                newBooking.clinicId(),
                newBooking.infections(),
                Instant.now()
        );
        var existingBooking = bookings.putIfAbsent(key, booking);
        if (Objects.nonNull(existingBooking)) {
            throw new RuntimeException("Key conflict; try again");
        }

        return booking;
    }

    private record Key(
            CustomerId customerId,
            BookingId bookingId
    ) {}
}
