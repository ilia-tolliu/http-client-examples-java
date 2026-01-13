package travelvac.client.monitoring;

public class HttpException extends RuntimeException {

  public HttpException(int statusCode) {
    var message = "HTTP error response: %d".formatted(statusCode);
    super(message);
  }
}
