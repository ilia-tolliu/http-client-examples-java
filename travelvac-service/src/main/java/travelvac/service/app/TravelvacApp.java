package travelvac.service.app;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.undertow.Undertow;
import travelvac.service.adapters.BookingRepositoryInMem;
import travelvac.service.adapters.ClinicRepositoryDemo;
import travelvac.service.adapters.RiskRepositoryDemo;
import travelvac.service.core.BookingService;
import travelvac.service.core.ClinicService;
import travelvac.service.core.RiskService;
import travelvac.service.handlers.TravelvacHandler;

import java.util.TimeZone;

public class TravelvacApp {

  static void main() {
    System.out.println("Hello from Travelvac!");

    var riskRepository = new RiskRepositoryDemo();
    var riskService = new RiskService(riskRepository);

    var clinicRepository = new ClinicRepositoryDemo();
    var clinicService = new ClinicService(clinicRepository);

    var bookingRepository = new BookingRepositoryInMem();
    var bookingService = new BookingService(bookingRepository);

    JsonMapper json = configureJsonMapper();

    var handler = new TravelvacHandler(json, riskService, clinicService, bookingService);

    Undertow server = Undertow.builder()
      .addHttpListener(8080, "localhost")
      .setHandler(handler)
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