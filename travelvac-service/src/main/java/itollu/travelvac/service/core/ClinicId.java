package itollu.travelvac.service.core;

import java.util.UUID;

public record ClinicId(UUID value) {
    private static final String PREFIX = "CLC-";

    public static ClinicId parse(String valueString) {
        var valueUuid = IdWithPrefix.parse(valueString, PREFIX);

        return new ClinicId(valueUuid);
    }

    public String format() {
        return IdWithPrefix.format(value, PREFIX);
    }
}
