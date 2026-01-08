package travelvac.service.http;

import travelvac.service.core.Booking;

import java.time.Instant;
import java.util.List;

record BookingJson (
    String bookingId,
    String reference,
    String clinicId,
    List<String> infections,
    Instant createdAt
) {
    static BookingJson fromBooking(Booking booking) {
        return new BookingJson(
                booking.bookingId().format(),
                booking.reference(),
                booking.clinicId().format(),
                booking.infections(),
                booking.createdAt()
        );
    }
}

