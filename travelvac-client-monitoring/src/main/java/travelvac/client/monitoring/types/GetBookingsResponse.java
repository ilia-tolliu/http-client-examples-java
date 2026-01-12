package travelvac.client.monitoring.types;

import java.util.List;

public record GetBookingsResponse(
  List<Booking> bookings
) {
}
