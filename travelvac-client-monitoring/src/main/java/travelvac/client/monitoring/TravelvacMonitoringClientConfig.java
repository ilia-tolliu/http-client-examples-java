package travelvac.client.monitoring;

import java.time.Duration;

public record TravelvacMonitoringClientConfig(
  String apiKey,
  String baseUrl,
  Duration connectTimeout,
  Duration getRisksTimeout,
  Duration getClinicsTimeout,
  Duration postBookingTimeout,
  Duration getBookingsTimeout,
  Duration getBookingTimeout
) {
}
