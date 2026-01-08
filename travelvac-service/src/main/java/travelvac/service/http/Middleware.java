package travelvac.service.http;

import java.util.UUID;

import io.undertow.server.handlers.encoding.ContentEncodingRepository;
import io.undertow.server.handlers.encoding.EncodingHandler;
import io.undertow.server.handlers.encoding.GzipEncodingProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.ExceptionHandler;
import io.undertow.server.handlers.accesslog.AccessLogHandler;
import io.undertow.util.AttachmentKey;
import io.undertow.util.HttpString;
import travelvac.service.ratelimiter.RateLimitExhaustedException;

public class Middleware {
  public static final HttpString REQUEST_ID_HEADER = new HttpString("X-Request-Id");
  private static final AttachmentKey<RequestId> REQUEST_ID_KEY = AttachmentKey.create(RequestId.class);

  private static final Logger LOG = LoggerFactory.getLogger("travelvac.service");

  public static AccessLogHandler withLogging(HttpHandler handler) {
    return new AccessLogHandler(
      handler,
      LOG::info,
      "%h %l %u %t \"%r\" %s %b %{o,X-Request-Id} \"%{i,Referer}\" \"%{i,User-Agent}\"",
      AccessLogHandler.class.getClassLoader()
    );
  }

  public static ExceptionHandler withExceptionHandler(JsonMapper json, HttpHandler handler) {
    var exceptionHandler = new ExceptionHandler(handler);

    exceptionHandler.addExceptionHandler(RateLimitExhaustedException.class, new RateLimitExceptionHandler());
    exceptionHandler.addExceptionHandler(ClientException.class, new ClientExceptionHandler(json));
    exceptionHandler.addExceptionHandler(Throwable.class, new InternalServerErrorHandler());

    return exceptionHandler;
  }

  public static HttpHandler withRequestId(HttpHandler handler) {
    return exchange -> {
      var requestId = RequestId.create();
      exchange.putAttachment(REQUEST_ID_KEY, requestId);
      exchange.getResponseHeaders().put(REQUEST_ID_HEADER, requestId.value);

      handler.handleRequest(exchange);
    };
  }

  public static HttpHandler withGzipEncoding(HttpHandler handler) {
    var encodingRepository = new ContentEncodingRepository();
    encodingRepository.addEncodingHandler("gzip", new GzipEncodingProvider(), 0);

    return new EncodingHandler(handler, encodingRepository);
  }

  public record RequestId(String value) {
    static RequestId create() {
      var requestId = "REQ-%s".formatted(UUID.randomUUID());

      return new RequestId(requestId);
    }
  }
}
