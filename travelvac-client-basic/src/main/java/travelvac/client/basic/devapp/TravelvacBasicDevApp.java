package travelvac.client.basic.devapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import travelvac.client.basic.TravelvacBasicClient;
import travelvac.client.basic.TravelvacBasicClientConfig;

public class TravelvacBasicDevApp {

  private static final Logger LOG = LoggerFactory.getLogger(TravelvacBasicClient.class);

  private static final String API_KEY = "Basic Q1VTLWVlMzA5NDM1LTY3NWItNGQ4Mi05ZTgxLWMxMzcxNWMxYjQ3YTptYW5kYXJpbmVzLXVuZGVyLWNvdmVy";

  private static final String BASE_URL = "http://localhost:8080";

  static void main() {
    LOG.info("Hello from Travelvac Basic Client!");

    var config = new TravelvacBasicClientConfig(API_KEY, BASE_URL);

    var client = new TravelvacBasicClient(config);


  }
}
