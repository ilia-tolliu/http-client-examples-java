package travelvac.service.ratelimiter;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiter {

    private final int bucketCapacity;

    private final Duration refillPeriod;

    private final ConcurrentHashMap<String, BucketOfTokens> buckets = new ConcurrentHashMap<>();

    public RateLimiter(int bucketCapacity, Duration refillPeriod) {
        this.bucketCapacity = bucketCapacity;
        this.refillPeriod = refillPeriod;
    }

    public void checkIn(String key) {
        var now = Instant.now();

        buckets.compute(key, (_, bucket) -> {
            if (Objects.isNull(bucket)) {
                return fullBucket().consumeToken();
            }
            if (now.minus(refillPeriod).isAfter(bucket.refilledAt)) {
                return fullBucket().consumeToken();
            }
            if (bucket.tokens > 0) {
                return bucket.consumeToken();
            }
            throw new RateLimitExhaustedException();
        });
    }

    private BucketOfTokens fullBucket() {
        return new BucketOfTokens(bucketCapacity, Instant.now());
    }

    private record BucketOfTokens(
        int tokens,
        Instant refilledAt
    ) {
        BucketOfTokens consumeToken() {
            return new BucketOfTokens(tokens - 1, refilledAt);
        }
    }
}
