package itollu.travelvac.service.adapters;

import itollu.travelvac.service.core.CountryCode;
import itollu.travelvac.service.core.Risk;
import itollu.travelvac.service.core.RiskRepository;

import java.util.List;

public class RiskRepositoryDemo implements RiskRepository {
    @Override
    public List<Risk> getRisks(CountryCode countryCode) {
        return List.of();
    }
}