package travelvac.service.http;

import travelvac.service.core.Booking;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

record BookingJson (
    String bookingId,
    String reference,
    String clinicId,
    List<String> infections,
    LocalDateTime scheduledAt,
    Instant createdAt
) {
    static BookingJson fromBooking(Booking booking) {
        return new BookingJson(
                booking.bookingId().format(),
                booking.reference(),
                booking.clinicId().format(),
                booking.infections(),
                booking.scheduledAt(),
                booking.createdAt()
        );
    }
}

