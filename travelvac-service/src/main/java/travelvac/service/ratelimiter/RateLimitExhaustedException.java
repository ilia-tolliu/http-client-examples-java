package travelvac.service.ratelimiter;

public class RateLimitExhaustedException extends RuntimeException {
    public RateLimitExhaustedException() {
        super("Rate limit exhausted");
    }
}
