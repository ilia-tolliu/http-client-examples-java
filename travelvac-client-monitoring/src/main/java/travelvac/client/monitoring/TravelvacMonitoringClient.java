package travelvac.client.monitoring;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import travelvac.client.monitoring.types.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.util.List;

import static java.net.http.HttpRequest.BodyPublishers;
import static java.net.http.HttpRequest.newBuilder;
import static java.net.http.HttpResponse.BodyHandlers;
import static travelvac.client.monitoring.MetricsServer.registry;

public class TravelvacMonitoringClient {

  private final TravelvacMonitoringClientConfig config;

  public TravelvacMonitoringClient(TravelvacMonitoringClientConfig config) {
    this.config = config;
  }

  // TODO check that URL is well formed

  public List<Risk> getRisks(String countryCode) throws Exception {
    registry.counter("travelvac.get_risks").increment();

    try (var httpClient = getHttpClient()) {
      var request = newBuilder()
        .GET()
        .header("Authorization", "Basic " + config.apiKey())
        .uri(URI.create("%s/countries/%s/risks".formatted(config.baseUrl(), countryCode)))
        .build();

      var response = httpClient.send(request, BodyHandlers.ofString());

      var json = buildJsonMapper();
      var responseBody = json.readValue(response.body(), GetRisksResponse.class);

      return responseBody.risks();
    }
  }

  public List<Clinic> getClinics(String countryCode) throws Exception {
    registry.counter("travelvac.get_clinics").increment();

    try (var httpClient = getHttpClient()) {
      var request = newBuilder()
        .GET()
        .header("Authorization", "Basic " + config.apiKey())
        .uri(URI.create("%s/countries/%s/clinics".formatted(config.baseUrl(), countryCode)))
        .build();

      var response = httpClient.send(request, BodyHandlers.ofString());

      var json = buildJsonMapper();
      var responseBody = json.readValue(response.body(), GetClinicsResponse.class);

      return responseBody.clinics();
    }
  }

  public Booking postBooking(NewBooking newBooking) throws Exception {
    registry.counter("travelvac.post_booking").increment();

    try (var httpClient = getHttpClient()) {
      var json = buildJsonMapper();
      var requestBody = json.writeValueAsString(newBooking);
      var request = newBuilder()
        .POST(BodyPublishers.ofString(requestBody))
        .header("Authorization", "Basic " + config.apiKey())
        .header("Content-Type", "application/json")
        .uri(URI.create("%s/bookings".formatted(config.baseUrl())))
        .build();

      var response = httpClient.send(request, BodyHandlers.ofString());
      return json.readValue(response.body(), Booking.class);
    }
  }

  public List<Booking> getBookings() throws Exception {
    registry.counter("travelvac.get_bookings").increment();

    try (var httpClient = getHttpClient()) {
      var request = newBuilder()
        .GET()
        .header("Authorization", "Basic " + config.apiKey())
        .uri(URI.create("%s/bookings".formatted(config.baseUrl())))
        .build();

      var json = buildJsonMapper();
      var response = httpClient.send(request, BodyHandlers.ofString());
      var responseBody = json.readValue(response.body(), GetBookingsResponse.class);

      return responseBody.bookings();
    }
  }

  public Booking getBooking(String bookingId) throws Exception {
    registry.counter("travelvac.get_booking").increment();

    try (var httpClient = getHttpClient()) {
      var request = newBuilder()
        .GET()
        .header("Authorization", "Basic " + config.apiKey())
        .uri(URI.create("%s/bookings/%s".formatted(config.baseUrl(), bookingId)))
        .build();

      var json = buildJsonMapper();
      var response = httpClient.send(request, BodyHandlers.ofString());

      return json.readValue(response.body(), Booking.class);
    }
  }


  HttpClient getHttpClient() {
    return HttpClient.newHttpClient();
  }

  JsonMapper buildJsonMapper() {
    return new JsonMapper().rebuild()
      .addModule(new JavaTimeModule())
      .build();
  }

}