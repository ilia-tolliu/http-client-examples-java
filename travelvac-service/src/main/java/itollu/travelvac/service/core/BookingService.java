package itollu.travelvac.service.core;

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
}
