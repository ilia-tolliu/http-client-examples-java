package itollu.travelvac.service.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class BookingService {
    private static final Logger LOG = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking bookAppointment(NewBooking newBooking, IdempotencyKey idempotencyKey) {
        var idempotentBookingId = getIdempotentBookingId(newBooking.customerId(), idempotencyKey);

        if (Objects.nonNull(idempotentBookingId)) {
            LOG.info(
                    "Idempotent booking [{}, {}]: using existing [{}]",
                    newBooking.customerId().format(),
                    idempotencyKey.format(),
                    idempotentBookingId.format()
            );
            return bookingRepository.getBooking(newBooking.customerId(), idempotentBookingId);
        }

        try {
            LOG.info(
                    "Idempotent booking [{}, {}]: creating new",
                    newBooking.customerId().format(),
                    idempotencyKey.format()
            );
            // TODO validate new booking
            return bookingRepository.createBooking(newBooking, idempotencyKey);
        } catch (Exception e) {
            var runtimeException = new RuntimeException("Failed to create booking", e);
            bookingRepository.setIdempotentException(newBooking.customerId(), idempotencyKey, runtimeException);

            throw runtimeException;
        }
    }

    private BookingId getIdempotentBookingId(CustomerId customerId, IdempotencyKey idempotencyKey) {
        while (true) {
            var idempotencyRecord = bookingRepository.checkInIdempotencyKey(customerId, idempotencyKey);
            if (Objects.isNull(idempotencyRecord)) {
                return null;
            }
            if (Objects.nonNull(idempotencyRecord.bookingId())) {
                return idempotencyRecord.bookingId();
            }
            if (Objects.nonNull(idempotencyRecord.exception())) {
                throw idempotencyRecord.exception();
            }
        }
    }

    public Booking getBooking(CustomerId customerId, BookingId bookingId) {
        return bookingRepository.getBooking(customerId, bookingId);
    }

    public List<Booking> getBookings(CustomerId customerId) {
        return bookingRepository.getBookings(customerId);
    }
}
