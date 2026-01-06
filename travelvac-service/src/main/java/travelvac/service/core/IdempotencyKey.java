package travelvac.service.core;

import java.util.UUID;

public record IdempotencyKey(UUID value) {
    private static final String PREFIX = "IDM-";

    public static IdempotencyKey generate() {
        var randomUuid = UUID.randomUUID();
        return new IdempotencyKey(randomUuid);
    }

    public static IdempotencyKey parse(String valueString) {
        var valueUuid = IdWithPrefix.parse(valueString, PREFIX);

        return new IdempotencyKey(valueUuid);
    }

    public String format() {
        return IdWithPrefix.format(value, PREFIX);
    }
}
