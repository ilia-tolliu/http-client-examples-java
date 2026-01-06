package itollu.travelvac.service.core;

import java.util.List;

public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking bookAppointment(NewBooking newBooking) {
        // TODO validate new booking
        return bookingRepository.createBooking(newBooking);
    }

    public Booking getBooking(CustomerId customerId, BookingId bookingId) {
        return bookingRepository.getBooking(customerId, bookingId);
    }

    public List<Booking> getBookings(CustomerId customerId) {
        return bookingRepository.getBookings(customerId);
    }
}
