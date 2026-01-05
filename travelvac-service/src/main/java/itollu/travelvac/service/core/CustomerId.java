package itollu.travelvac.service.core;

import java.util.UUID;

public record CustomerId(UUID value) {

    private static final String PREFIX = "CUS-";

    public static CustomerId parse(String valueString) {
        var valueUuid = IdWithPrefix.parse(valueString, PREFIX);

        return new CustomerId(valueUuid);
    }

    public String format() {
        return IdWithPrefix.format(value, PREFIX);
    }
}
