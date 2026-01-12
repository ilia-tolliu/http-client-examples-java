package travelvac.client.monitoring;

import com.sun.net.httpserver.HttpServer;
import io.micrometer.prometheusmetrics.PrometheusConfig;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MetricsServer {
    public static final PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

    public Thread run() {
        try {
            var server = HttpServer.create(new InetSocketAddress(8081), 0);
            server.createContext("/prometheus", httpExchange -> {
                var response = registry.scrape();
                httpExchange.sendResponseHeaders(200, response.getBytes().length);
                try (var os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            });

            var serverThread = new Thread(server::start);
            serverThread.start();
            return serverThread;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
