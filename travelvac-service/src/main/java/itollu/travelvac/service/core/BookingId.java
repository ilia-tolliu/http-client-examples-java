package itollu.travelvac.service.core;

import java.util.UUID;

public record BookingId(UUID value) {

    private static final String PREFIX = "BK-";

    public static BookingId generate() {
        var randomUuid = UUID.randomUUID();
        return new BookingId(randomUuid);
    }

    public static BookingId parse(String valueString) {
        var valueUuid = IdWithPrefix.parse(valueString, PREFIX);

        return new BookingId(valueUuid);
    }

    public String format() {
        return IdWithPrefix.format(value, PREFIX);
    }
}
