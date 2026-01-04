package itollu.travelvac.service.handlers;

import com.fasterxml.jackson.databind.json.JsonMapper;
import io.undertow.server.RoutingHandler;
import itollu.travelvac.service.core.ClinicService;
import itollu.travelvac.service.core.RiskService;

import static io.undertow.Handlers.routing;

public class Routes {
    private final JsonMapper json;

    private final RiskService riskService;

    private final ClinicService clinicService;

    public Routes(
            JsonMapper json,
            RiskService riskService,
            ClinicService clinicService
    ) {
        this.json = json;
        this.riskService = riskService;
        this.clinicService = clinicService;
    }

    public RoutingHandler buildRouter() {
        return routing()
                .get("status", new GetStatus(json))
                .get("countries/{country}/risks", new GetRisks(riskService, json))
                .get("countries/{country}/clinics", new GetClinics(clinicService, json));
    }
}
