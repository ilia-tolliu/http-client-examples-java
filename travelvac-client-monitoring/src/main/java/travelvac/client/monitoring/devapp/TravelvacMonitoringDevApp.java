package travelvac.client.monitoring.devapp;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import travelvac.client.monitoring.MetricsServer;
import travelvac.client.monitoring.TravelvacMonitoringClient;
import travelvac.client.monitoring.TravelvacMonitoringClientConfig;
import travelvac.client.monitoring.types.NewBooking;

public class TravelvacMonitoringDevApp {

  private static final Logger LOG = LoggerFactory.getLogger(TravelvacMonitoringDevApp.class);

  private static final String API_KEY = "Q1VTLWVlMzA5NDM1LTY3NWItNGQ4Mi05ZTgxLWMxMzcxNWMxYjQ3YTptYW5kYXJpbmVzLXVuZGVyLWNvdmVy";

  private static final String BASE_URL = "http://localhost:8080";

  static void main() throws Exception {
    LOG.info("Hello! This is Travelvac Monitoring Client demo application.");
    var metricsServer = new MetricsServer();
    var metricsServerThread = metricsServer.run();

    var config = new TravelvacMonitoringClientConfig(API_KEY, BASE_URL);
    var client = new TravelvacMonitoringClient(config);

    LOG.info("GET /countries/{countryCode}/risks");
    var risks = client.getRisks("TGO");
    LOG.info("Risks: {}", risks);

    LOG.info("GET /countries/{countryCode}/clinics");
    var clinics = client.getClinics("SWE");
    LOG.info("Clinics: {}", clinics);

    LOG.info("POST /bookings");
    var newBooking = new NewBooking("booking001", "CLC-2950268c-d0bd-4779-b314-35227f949c50", LocalDateTime.parse("2026-01-25T13:25:00"), List.of("Hepatitis A"));
    var booking = client.postBooking(newBooking);
    LOG.info("Booking: {}", booking);

    LOG.info("GET /bookings");
    var bookings = client.getBookings();
    LOG.info("Bookings: {}", bookings);

    LOG.info("GET /bookings/{bookingId}");
    var bookingById = client.getBooking(booking.bookingId());
    LOG.info("Booking by ID: {}", bookingById);

    metricsServerThread.join();
  }
}
