package itollu.travelvac.service.core;

import java.util.List;

public interface BookingRepository {

    Booking getBooking(CustomerId customerId, BookingId bookingId);

    List<Booking> getBookings(CustomerId customerId);

    Booking createBooking(NewBooking newBooking);
}
