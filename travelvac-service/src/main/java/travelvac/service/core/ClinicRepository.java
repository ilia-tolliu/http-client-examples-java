package travelvac.service.core;

import java.util.List;

public interface ClinicRepository {
    List<Clinic> getClinics(CountryCode countryCode);
}
