package travelvac.client.monitoring;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.Timer.Sample;
import travelvac.client.monitoring.types.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import static java.net.http.HttpRequest.BodyPublishers;
import static java.net.http.HttpRequest.newBuilder;
import static java.net.http.HttpResponse.BodyHandlers;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.nonNull;
import static travelvac.client.monitoring.MetricsServer.registry;
import static travelvac.client.monitoring.Outcome.FAILURE;
import static travelvac.client.monitoring.Outcome.SUCCESS;

public class TravelvacMonitoringClient {

  private final TravelvacMonitoringClientConfig config;

  public TravelvacMonitoringClient(TravelvacMonitoringClientConfig config) {
    this.config = config;
  }

  // TODO check that URL is well formed

  public List<Risk> getRisks(String countryCode) {
    var action = "get_risks";
    var timerSample = Timer.start(registry);

    try {
      var request = newBuilder()
        .GET()
        .header("Authorization", "Basic " + config.apiKey())
        .uri(URI.create("%s/countries/%s/risks".formatted(config.baseUrl(), countryCode)))
        .timeout(config.getRisksTimeout())
        .build();

      var response = sendRequest(request);
      var responseBody = readResponseBody(response, GetRisksResponse.class);

      timerSample.stop(getTimer(action, SUCCESS, response.statusCode()));
      return responseBody.risks();
    } catch (Exception e) {
      throw registerException(action, timerSample, e);
    }
  }

  public List<Clinic> getClinics(String countryCode) {
    var action = "get_clinics";
    var timerSample = Timer.start(registry);

    try {
      var request = newBuilder()
        .GET()
        .header("Authorization", "Basic " + config.apiKey())
        .uri(URI.create("%s/countries/%s/clinics".formatted(config.baseUrl(), countryCode)))
        .timeout(config.getClinicsTimeout())
        .build();

      var response = sendRequest(request);
      var responseBody = readResponseBody(response, GetClinicsResponse.class);

      timerSample.stop(getTimer(action, SUCCESS, response.statusCode()));
      return responseBody.clinics();
    } catch (Exception e) {
      throw registerException(action, timerSample, e);
    }
  }

  public Booking postBooking(NewBooking newBooking) {
    var action = "post_booking";
    var timerSample = Timer.start(registry);

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
      var responseBody = readResponseBody(response, Booking.class);

      timerSample.stop(getTimer(action, SUCCESS, response.statusCode()));
      return responseBody;
    } catch (Exception e) {
      throw registerException(action, timerSample, e);
    }
  }

  public List<Booking> getBookings() {
    var action = "get_bookings";
    var timerSample = Timer.start(registry);

    try {
      var request = newBuilder()
        .GET()
        .header("Authorization", "Basic " + config.apiKey())
        .uri(URI.create("%s/bookings".formatted(config.baseUrl())))
        .timeout(config.getBookingsTimeout())
        .build();

      var response = sendRequest(request);
      var responseBody = readResponseBody(response, GetBookingsResponse.class);

      timerSample.stop(getTimer(action, SUCCESS, response.statusCode()));
      return responseBody.bookings();
    } catch (Exception e) {
      throw registerException(action, timerSample, e);
    }
  }

  public Booking getBooking(String bookingId) {
    var action = "get_booking";
    var timerSample = Timer.start(registry);

    try {
      var request = newBuilder()
        .GET()
        .header("Authorization", "Basic " + config.apiKey())
        .uri(URI.create("%s/bookings/%s".formatted(config.baseUrl(), bookingId)))
        .timeout(config.postBookingTimeout())
        .build();

      var response = sendRequest(request);
      var responseBody = readResponseBody(response, Booking.class);

      timerSample.stop(getTimer(action, SUCCESS, response.statusCode()));
      return responseBody;
    } catch (Exception e) {
      throw registerException(action, timerSample, e);
    }
  }

  private HttpResponse<String> sendRequest(HttpRequest request) {
    try (var httpClient = getHttpClient()) {
      var response = httpClient.send(request, BodyHandlers.ofString(UTF_8));
      var statusCode = response.statusCode();
      if (statusCode >= 200 && statusCode <= 299) {
        return response;
      }
      throw new HttpException(statusCode);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private <T> T readResponseBody(HttpResponse<String> response, Class<T> targetClass) {
    var json = getJsonMapper();
    try {
      return json.readValue(response.body(), targetClass);
    } catch (Exception e) {
      throw new ResponseReadException(response.statusCode(), e);
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

  private Timer getTimer(String action, Outcome outcome, @Nullable Integer statusCode) {
    var statusCodeStr = "N/A";
    if (nonNull(statusCode)) {
      statusCodeStr = statusCode.toString();
    }

    var tags = Tags.of(
      "action", action,
      "outcome", outcome.toString(),
      "status", statusCodeStr
    );

    return Timer.builder("atlantis.travelvac")
      .publishPercentiles(0.5, 0.95, 0.99)
      .tags(tags)
      .register(registry);
  }

  private TravelvacException registerException(String action, Sample timerSample, Exception e) {
    Integer statusCode = null;
    if (e instanceof HttpException httpException) {
      statusCode = httpException.getStatusCode();
    }
    if (e instanceof ResponseReadException responseReadException) {
      statusCode = responseReadException.getStatusCode();
    }

    timerSample.stop(getTimer(action, FAILURE, statusCode));

    var message = "failed to complete [%s]".formatted(action);
    return new travelvac.client.monitoring.TravelvacException(message, e);
  }

}