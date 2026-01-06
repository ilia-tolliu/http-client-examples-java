package travelvac.service.core;

import java.util.List;

public class RiskService {
    private final RiskRepository riskRepository;

    public RiskService(RiskRepository riskRepository) {
        this.riskRepository = riskRepository;
    }

    public List<Risk> getRisks(CustomerId customerId, CountryCode countryCode) {
        // TODO check if customer is eligible to list risks
        return riskRepository.getRisks(countryCode);
    }
}
