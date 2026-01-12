package travelvac.client.monitoring;

public class HttpException extends RuntimeException {

  private final int statusCode;

  public HttpException(int statusCode) {
    var message = "HTTP request error: %d".formatted(statusCode);
    super(message);

    this.statusCode = statusCode;
  }

  public int getStatusCode() {
    return statusCode;
  }
}
