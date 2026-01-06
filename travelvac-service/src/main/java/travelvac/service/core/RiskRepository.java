package travelvac.service.core;

import java.util.List;

public interface RiskRepository {
    List<Risk> getRisks(CountryCode countryCode);
}
