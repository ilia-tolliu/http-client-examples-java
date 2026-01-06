package travelvac.service.handlers;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.AttachmentKey;
import travelvac.service.core.CustomerId;

import java.util.Base64;
import java.util.UUID;

import static io.undertow.util.Headers.AUTHORIZATION;
import static io.undertow.util.StatusCodes.BAD_REQUEST;
import static io.undertow.util.StatusCodes.UNAUTHORIZED;
import static travelvac.service.handlers.HandlerUtils.requireRequestHeader;
import static java.nio.charset.StandardCharsets.UTF_8;

public class AuthUtils {
    static final AttachmentKey<CustomerId> CUSTOMER_ID_KEY = AttachmentKey.create(CustomerId.class);

    static final String TEST_CUSTOMER_ID = "CUST_ee309435-675b-4d82-9e81-c13715c1b47a";

    static final String TEST_CUSTOMER_SECRET = "mandarines-under-cover";

    private static final Credentials TEST_CREDENTIALS = new Credentials(TEST_CUSTOMER_ID, TEST_CUSTOMER_SECRET);

    static CustomerId authenticate(HttpServerExchange exchange) {
        var authHeaderValue = requireRequestHeader(AUTHORIZATION, exchange);
        var credentials = parseCredentials(authHeaderValue);
        var customerId = authenticateCredentials(credentials);
        exchange.putAttachment(CUSTOMER_ID_KEY, customerId);

        return customerId;
    }

    private static Credentials parseCredentials(String authValue) {
        var prefix = "Basic ";
        if (!authValue.startsWith(prefix)) {
            throw new ClientException(BAD_REQUEST, "Basic authentication scheme is expected");
        }

        var credentialsBase64 = authValue.substring(prefix.length());
        var credentialsBytes = Base64.getDecoder().decode(credentialsBase64);
        var credentialsString = new String(credentialsBytes, UTF_8);
        var keySecret = credentialsString.split(":");
        if (keySecret.length != 2) {
            throw new ClientException(BAD_REQUEST, "Invalid basic authentication credentials");
        }

        return new Credentials(keySecret[0], keySecret[1]);
    }

    private static CustomerId authenticateCredentials(Credentials credentials) {
        if (!credentials.equals(TEST_CREDENTIALS)) {
            throw new ClientException(UNAUTHORIZED, "Wrong credentials");
        }

        return parseCustomerId(credentials.key);
    }

    private static CustomerId parseCustomerId(String credentialKey) {
        var prefix = "CUST_";
        var idString = credentialKey.substring(prefix.length());
        var uuid = UUID.fromString(idString);
        return new CustomerId(uuid);
    }

    private record Credentials(
            String key,
            String secret
    ) {}
}
