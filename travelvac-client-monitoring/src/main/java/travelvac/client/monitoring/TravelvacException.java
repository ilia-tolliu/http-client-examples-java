package travelvac.client.monitoring;

public class TravelvacException extends RuntimeException {

  public TravelvacException(String message, Exception reason) {
    super(message, reason);
  }
}
