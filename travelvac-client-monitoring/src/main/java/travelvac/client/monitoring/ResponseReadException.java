package travelvac.client.monitoring;

public class ResponseReadException extends RuntimeException {

  public ResponseReadException(Exception reason) {
    super(reason);
  }
}
