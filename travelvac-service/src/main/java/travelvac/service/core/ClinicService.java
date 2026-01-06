package travelvac.service.core;

import java.util.List;

public class ClinicService {
    private final ClinicRepository clinicRepository;

    public ClinicService(ClinicRepository clinicRepository) {
        this.clinicRepository = clinicRepository;
    }

    public List<Clinic> getClinics(CustomerId customerId, CountryCode countryCode) {
        // TODO check if customer is eligible to list clinics
        return clinicRepository.getClinics(countryCode);
    }
}
