package itollu.travelvac.service.app;

import java.util.TimeZone;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.undertow.Undertow;
import itollu.travelvac.service.adapters.ClinicRepositoryDemo;
import itollu.travelvac.service.adapters.RiskRepositoryDemo;
import itollu.travelvac.service.core.ClinicService;
import itollu.travelvac.service.core.RiskService;
import itollu.travelvac.service.handlers.Routes;

import static itollu.travelvac.service.handlers.Middleware.withExceptionHandler;
import static itollu.travelvac.service.handlers.Middleware.withLogging;
import static itollu.travelvac.service.handlers.Middleware.withRequestId;

public class TravelvacApp {

  static void main() {
    System.out.println("Hello from Travelvac!");

    var riskRepository = new RiskRepositoryDemo();
    var riskService = new RiskService(riskRepository);

    var clinicRepository = new ClinicRepositoryDemo();
    var clinicService = new ClinicService(clinicRepository);

    JsonMapper json = configureJsonMapper();
    Routes routes = new Routes(json, riskService, clinicService);
    var router = routes.buildRouter();

    Undertow server = Undertow.builder()
      .addHttpListener(8080, "localhost")
      .setHandler(
        withRequestId(
          withExceptionHandler(
            json,
            withLogging(
              router
            )
          )
        )
      )
      .build();

    server.start();
  }

  static JsonMapper configureJsonMapper() {
    var dateFormat = new StdDateFormat()
      .withTimeZone(TimeZone.getTimeZone("UTC"));

    return new JsonMapper().rebuild()
      .enable(SerializationFeature.INDENT_OUTPUT) // pretty print
      .addModule(new JavaTimeModule())
      .defaultDateFormat(dateFormat)
      .build();
  }
}