package travelvac.client.monitoring;

public class TravelvacException extends RuntimeException {

  public TravelvacException(Exception reason) {
    super(reason);
  }
}
