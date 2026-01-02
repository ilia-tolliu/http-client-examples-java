package itollu.travelvac.service.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.accesslog.AccessLogHandler;

public class Handlers {
  private static final Logger LOG = LoggerFactory.getLogger("itollu.travelvac.service");

  public static AccessLogHandler withLogging(HttpHandler handler) {
    return new AccessLogHandler(
      handler,
      LOG::info,
      "combined",
      AccessLogHandler.class.getClassLoader()
    );
  }
}
