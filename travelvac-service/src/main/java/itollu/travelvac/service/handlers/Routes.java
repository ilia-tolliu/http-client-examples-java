package itollu.travelvac.service.handlers;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.undertow.server.RoutingHandler;
import itollu.travelvac.service.core.RiskService;

import static io.undertow.Handlers.routing;

public class Routes {
    private final JsonMapper json;

    private final RiskService riskService;

    public Routes(JsonMapper json, RiskService riskService) {
        this.json = json;
        this.riskService = riskService;
    }

    public RoutingHandler buildRouter() {
        return routing()
                .get("status", new GetStatus(json))
                .get("destination/{country}/risks", new GetRisks(riskService, json));
    }
}
