package travelvac.client.basic.types;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public record Booking(
  String bookingId,
  String reference,
  String clinicId,
  LocalDateTime scheduledAt,
  List<String> infections,
  Instant createdAt
) {
}
