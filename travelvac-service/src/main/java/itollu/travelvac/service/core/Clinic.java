package itollu.travelvac.service.core;

public record Clinic(
        ClinicId clinicId,
        CountryCode countryCode,
        String city,
        String address,
        String description
) {
}
