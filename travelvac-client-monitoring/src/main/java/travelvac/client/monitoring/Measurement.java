package travelvac.client.monitoring;

import java.time.Instant;

class Measurement {

  final String action;

  final Instant start = Instant.now();

  Integer statusCode;

  Exception exception;

  public Measurement(String action) {
    this.action = action;
  }
}
