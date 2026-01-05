package itollu.travelvac.service.core;

public interface BookingRepository {

    Booking getBooking(CustomerId customerId, BookingId bookingId);

    Booking createBooking(NewBooking newBooking);
}
