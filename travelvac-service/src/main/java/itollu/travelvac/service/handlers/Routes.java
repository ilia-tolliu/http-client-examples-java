package itollu.travelvac.service.handlers;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.undertow.server.RoutingHandler;

import static io.undertow.Handlers.routing;

public class Routes {
  private final JsonMapper json;

  public Routes(JsonMapper json) {
    this.json = json;
  }

  public RoutingHandler buildRouter() {
    return routing()
      .get("status", new GetStatus(json));
  }
}
