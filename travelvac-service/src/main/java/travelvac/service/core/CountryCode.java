package travelvac.service.core;

import java.util.Locale;

import static java.util.Locale.IsoCountryCode.PART1_ALPHA3;

public record CountryCode(String alpha3) {
    public CountryCode {
        var countryCodes = Locale.getISOCountries(PART1_ALPHA3);
        if (!countryCodes.contains(alpha3)) {
            throw new IllegalDomainValue("ISO3166-1 alpha-3 country code", alpha3);
        }
    }
}
