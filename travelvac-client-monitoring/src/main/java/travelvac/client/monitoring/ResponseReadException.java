package travelvac.client.monitoring;

public class ResponseReadException extends RuntimeException {

  private final int statusCode;

  public ResponseReadException(int statusCode, Exception reason) {
    var message = "Failed to read response [%d]".formatted(statusCode);
    super(message, reason);

    this.statusCode = statusCode;
  }

  public int getStatusCode() {
    return statusCode;
  }
}
