package itollu.travelvac.service.adapters;

import itollu.travelvac.service.core.*;

import java.util.List;

public class ClinicRepositoryDemo implements ClinicRepository {
    @Override
    public List<Clinic> getClinics(CountryCode countryCode) {
        return List.of();
    }
}