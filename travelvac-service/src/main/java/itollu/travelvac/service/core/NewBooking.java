package itollu.travelvac.service.core;

import java.time.LocalDateTime;
import java.util.List;

public record NewBooking(
        CustomerId customerId,
        String reference,
        ClinicId clinicId,
        LocalDateTime scheduledAt,
        List<String> infections
) {
}
