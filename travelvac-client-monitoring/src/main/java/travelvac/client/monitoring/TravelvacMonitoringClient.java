package travelvac.client.monitoring;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import travelvac.client.monitoring.types.Booking;
import travelvac.client.monitoring.types.Clinic;
import travelvac.client.monitoring.types.GetBookingsResponse;
import travelvac.client.monitoring.types.GetClinicsResponse;
import travelvac.client.monitoring.types.GetRisksResponse;
import travelvac.client.monitoring.types.NewBooking;
import travelvac.client.monitoring.types.Risk;

import static java.net.http.HttpRequest.BodyPublishers;
import static java.net.http.HttpRequest.newBuilder;
import static java.net.http.HttpResponse.BodyHandlers;
import static java.nio.charset.StandardCharsets.UTF_8;
import static travelvac.client.monitoring.MetricsServer.registry;

public class TravelvacMonitoringClient {

  private final TravelvacMonitoringClientConfig config;

  public TravelvacMonitoringClient(TravelvacMonitoringClientConfig config) {
    this.config = config;
  }

  // TODO check that URL is well formed

  public List<Risk> getRisks(String countryCode) {
    var measurement = new Measurement("get_risks");

    try {
      var request = newBuilder()
        .GET()
        .header("Authorization", "Basic " + config.apiKey())
        .uri(URI.create("%s/countries/%s/risks".formatted(config.baseUrl(), countryCode)))
        .timeout(config.getRisksTimeout())
        .build();

      var response = sendRequest(request);
      measurement.statusCode = response.statusCode();
      var responseBody = readResponseBody(response, GetRisksResponse.class);

      return responseBody.risks();
    } catch (Exception e) {
      measurement.exception = e;
      throw new TravelvacException(e);
    } finally {
      recordMeasurement(measurement);
    }
  }

  public List<Clinic> getClinics(String countryCode) {
    var measurement = new Measurement("get_clinics");

    try {
      var request = newBuilder()
        .GET()
        .header("Authorization", "Basic " + config.apiKey())
        .uri(URI.create("%s/countries/%s/clinics".formatted(config.baseUrl(), countryCode)))
        .timeout(config.getClinicsTimeout())
        .build();

      var response = sendRequest(request);
      measurement.statusCode = response.statusCode();
      var responseBody = readResponseBody(response, GetClinicsResponse.class);

      return responseBody.clinics();
    } catch (Exception e) {
      measurement.exception = e;
      throw new TravelvacException(e);
    } finally {
      recordMeasurement(measurement);
    }
  }

  public Booking postBooking(NewBooking newBooking) {
    var measurement = new Measurement("post_booking");

    try {
      var json = getJsonMapper();
      var requestBody = json.writeValueAsString(newBooking);
      var request = newBuilder()
        .POST(BodyPublishers.ofString(requestBody))
        .header("Authorization", "Basic " + config.apiKey())
        .header("Content-Type", "application/json")
        .uri(URI.create("%s/bookings".formatted(config.baseUrl())))
        .timeout(config.postBookingTimeout())
        .build();

      var response = sendRequest(request);
      measurement.statusCode = response.statusCode();

      return readResponseBody(response, Booking.class);
    } catch (Exception e) {
      measurement.exception = e;
      throw new TravelvacException(e);
    } finally {
      recordMeasurement(measurement);
    }
  }

  public List<Booking> getBookings() {
    var measurement = new Measurement("get_bookings");

    try {
      var request = newBuilder()
        .GET()
        .header("Authorization", "Basic " + config.apiKey())
        .uri(URI.create("%s/bookings".formatted(config.baseUrl())))
        .timeout(config.getBookingsTimeout())
        .build();

      var response = sendRequest(request);
      measurement.statusCode = response.statusCode();
      var responseBody = readResponseBody(response, GetBookingsResponse.class);

      return responseBody.bookings();
    } catch (Exception e) {
      measurement.exception = e;
      throw new TravelvacException(e);
    } finally {
      recordMeasurement(measurement);
    }
  }

  public Booking getBooking(String bookingId) {
    var measurement = new Measurement("get_booking");

    try {
      var request = newBuilder()
        .GET()
        .header("Authorization", "Basic " + config.apiKey())
        .uri(URI.create("%s/bookings/%s".formatted(config.baseUrl(), bookingId)))
        .timeout(config.postBookingTimeout())
        .build();

      var response = sendRequest(request);
      measurement.statusCode = response.statusCode();

      return readResponseBody(response, Booking.class);
    } catch (Exception e) {
      measurement.exception = e;
      throw new TravelvacException(e);
    } finally {
      recordMeasurement(measurement);
    }
  }

  private HttpResponse<String> sendRequest(HttpRequest request) {
    try (var httpClient = getHttpClient()) {
      return httpClient.send(request, BodyHandlers.ofString(UTF_8));
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private <T> T readResponseBody(HttpResponse<String> response, Class<T> targetClass) {
    var statusCode = response.statusCode();
    if (statusCode < 200 || statusCode > 299) {
      throw new HttpException(statusCode);
    }

    var json = getJsonMapper();
    try {
      return json.readValue(response.body(), targetClass);
    } catch (JsonProcessingException e) {
      throw new ResponseReadException(e);
    }
  }

  HttpClient getHttpClient() {
    return HttpClient.newBuilder()
      .connectTimeout(config.connectTimeout())
      .build();
  }

  JsonMapper getJsonMapper() {
    return new JsonMapper().rebuild()
      .addModule(new JavaTimeModule())
      .build();
  }

  private void recordMeasurement(Measurement measurement) {
    var outcome = Objects.isNull(measurement.exception) ? "success" : "failure";
    var status = Optional.ofNullable(measurement.statusCode)
      .map(Object::toString)
      .orElse("none");
    var tags = Tags.of(
      "action", measurement.action,
      "outcome", outcome,
      "status", status
    );

    Timer.builder("atlantis.travelvac")
      .publishPercentiles(0.5, 0.95, 0.99)
      .tags(tags)
      .register(registry)
      .record(Duration.between(measurement.start, Instant.now()));
  }
}