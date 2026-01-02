package itollu.travelvac.service.handlers;

import io.undertow.server.RoutingHandler;
import io.undertow.util.Headers;

import static io.undertow.Handlers.routing;

public class Routes {

  public static RoutingHandler buildRouter() {
    return routing()
      .get("status", new GetStatus());
  }
}
