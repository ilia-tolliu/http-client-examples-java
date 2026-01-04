package itollu.travelvac.service.adapters;

import itollu.travelvac.service.core.*;

import java.util.List;
import java.util.UUID;

public class ClinicRepositoryDemo implements ClinicRepository {
    @Override
    public List<Clinic> getClinics(CountryCode countryCode) {
        return CLINICS.stream()
                .filter(clinic -> clinic.countryCode().equals(countryCode))
                .toList();
    }

    private static final List<Clinic> CLINICS = List.of(
            new Clinic(
                    new ClinicId(UUID.fromString("2950268c-d0bd-4779-b314-35227f949c50")),
                    new CountryCode("SWE"),
                    "Stockholm",
                    "Brommaplan 403, Bromma",
                    "VaccindDirect, Bromma"
            ),
            new Clinic(
                    new ClinicId(UUID.fromString("a3e228b4-2333-4963-94a2-6089d2de962d")),
                    new CountryCode("SWE"),
                    "Stockholm",
                    "Slöjdgatan 9, Stockholm",
                    "VaccindDirect, Hötorget"
            )
    );
}