package travelvac.client.basic.types;

import java.util.List;

public record GetBookingsResponse(
  List<Booking> bookings
) {
}
