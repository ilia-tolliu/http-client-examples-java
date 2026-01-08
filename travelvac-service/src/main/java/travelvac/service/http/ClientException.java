package travelvac.service.http;

public class ClientException extends RuntimeException {
    private final int statusCode;

    private final String clientMessage;

    public ClientException(int statusCode, String logMessage, String clientMessage) {
        var formattedLogMessage = "%d: %s".formatted(statusCode, logMessage);
        super(formattedLogMessage);
        this.statusCode = statusCode;
        this.clientMessage = clientMessage;
    }

    public ClientException(int statusCode, String message) {
        this(statusCode, message, message);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getClientMessage() {
        return clientMessage;
    }
}
