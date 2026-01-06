package travelvac.service.core;

import java.util.UUID;

class IdWithPrefix {
    static UUID parse(String valueString, String prefix) {
        if (!valueString.startsWith(prefix)) {
            var expected = "valid prefixed UUID like [%s00000000-0000-0000-0000-000000000000]".formatted(prefix);
            throw new IllegalDomainValue(expected, valueString);
        }
        var uuidString = valueString.substring(prefix.length());
        try {
            return UUID.fromString(uuidString);
        } catch (Exception e) {
            throw new IllegalDomainValue("valid UUID string after prefix", uuidString);
        }
    }

    static String format(UUID valueUuid, String prefix) {
        return "%s%s".formatted(prefix, valueUuid);
    }
}
