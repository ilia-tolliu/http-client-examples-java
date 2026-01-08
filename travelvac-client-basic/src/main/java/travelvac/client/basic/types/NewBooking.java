package travelvac.client.basic.types;

import java.time.LocalDateTime;
import java.util.List;

public record NewBooking(
  String reference,
  String clinicId,
  LocalDateTime scheduledAt,
  List<String> infections
) {
}
