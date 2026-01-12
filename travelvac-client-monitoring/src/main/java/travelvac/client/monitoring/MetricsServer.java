package travelvac.client.monitoring;

import com.sun.net.httpserver.HttpServer;
import io.micrometer.prometheusmetrics.PrometheusConfig;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetricsServer {

  private static final Logger LOG = LoggerFactory.getLogger(MetricsServer.class);

  private static final int PORT = 9990;

  public static final PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

  public Thread run() {
    try {
      var server = HttpServer.create(new InetSocketAddress(PORT), 0);
      server.createContext("/metrics", httpExchange -> {
        var response = registry.scrape();
        httpExchange.getResponseHeaders().add("Content-Type", "text/plain; version=0.0.4");
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        try (var os = httpExchange.getResponseBody()) {
          os.write(response.getBytes());
        }
      });

      var serverThread = new Thread(server::start);
      serverThread.start();
      LOG.info("Metrics server started on port {}", PORT);

      return serverThread;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
