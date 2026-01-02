package itollu.travelvac.service.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.ExceptionHandler;
import io.undertow.server.handlers.accesslog.AccessLogHandler;

public class Middleware {
  private static final Logger LOG = LoggerFactory.getLogger("itollu.travelvac.service");

  public static AccessLogHandler withLogging(HttpHandler handler) {
    return new AccessLogHandler(
      handler,
      LOG::info,
      "combined",
      AccessLogHandler.class.getClassLoader()
    );
  }

  public static ExceptionHandler withExceptionHandler(JsonMapper json, HttpHandler handler) {
    var exceptionHandler = new ExceptionHandler(handler);

    exceptionHandler.addExceptionHandler(Throwable.class, new InternalServerErrorHandler());

    return exceptionHandler;
  }
}
