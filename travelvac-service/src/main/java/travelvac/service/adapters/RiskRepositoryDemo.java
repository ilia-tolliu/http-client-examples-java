package travelvac.service.adapters;

import travelvac.service.core.CountryCode;
import travelvac.service.core.Risk;
import travelvac.service.core.RiskRepository;

import java.util.List;

public class RiskRepositoryDemo implements RiskRepository {
    @Override
    public List<Risk> getRisks(CountryCode countryCode) {
        return INFECTION_DATA.stream()
                .filter(data -> data.countries.contains(countryCode))
                .map(InfectionData::toRisk)
                .toList();
    }

    private static final List<InfectionData> INFECTION_DATA = List.of(
            new InfectionData(
                    "Hepatitis A",
                    "Hepatitis A is a viral infection transmitted through contaminated food and water or by direct contact with an infectious person. Symptoms are often mild or absent in young children, but the disease can be more serious with advancing age. Recovery can vary from weeks to months. Following hepatitis A infection immunity is lifelong.",
                    List.of(
                            new CountryCode("AFG"),
                            new CountryCode("ALB"),
                            new CountryCode("DZA"),
                            new CountryCode("ARM"),
                            new CountryCode("AZE"),
                            new CountryCode("BLR"),
                            new CountryCode("BGR"),
                            new CountryCode("GEO"),
                            new CountryCode("IRN"),
                            new CountryCode("IRQ"),
                            new CountryCode("JOR"),
                            new CountryCode("MDA"),
                            new CountryCode("MAR"),
                            new CountryCode("TGO")
                    )
            ),
            new InfectionData(
                    "Tetanus",
                    "Tetanus is caused by a toxin released from Clostridium tetani bacteria and occurs worldwide. Tetanus bacteria are present in soil and manure and may be introduced through open wounds such as a puncture wound, burn or scratch.",
                    List.of(
                            new CountryCode("AFG"),
                            new CountryCode("ALB"),
                            new CountryCode("DZA"),
                            new CountryCode("AND"),
                            new CountryCode("AUT"),
                            new CountryCode("ARM"),
                            new CountryCode("AZE"),
                            new CountryCode("BLR"),
                            new CountryCode("BEL"),
                            new CountryCode("BIH"),
                            new CountryCode("BGR"),
                            new CountryCode("HRV"),
                            new CountryCode("CYP"),
                            new CountryCode("CZE"),
                            new CountryCode("DNK"),
                            new CountryCode("EST"),
                            new CountryCode("FIN"),
                            new CountryCode("FRA"),
                            new CountryCode("GEO"),
                            new CountryCode("DEU"),
                            new CountryCode("GIB"),
                            new CountryCode("MLT"),
                            new CountryCode("GRC"),
                            new CountryCode("HUN"),
                            new CountryCode("IRN"),
                            new CountryCode("IRQ"),
                            new CountryCode("IRL"),
                            new CountryCode("ISR"),
                            new CountryCode("ITA"),
                            new CountryCode("JOR"),
                            new CountryCode("LVA"),
                            new CountryCode("LIE"),
                            new CountryCode("LTU"),
                            new CountryCode("LUX"),
                            new CountryCode("MDA"),
                            new CountryCode("MAR"),
                            new CountryCode("TGO")
                    )
            ),
            new InfectionData(
                    "Typhoid",
                    "Typhoid is a bacterial infection transmitted through contaminated food and water. Previous typhoid illness may only partially protect against re-infection.",
                    List.of(
                            new CountryCode("AFG"),
                            new CountryCode("ARM"),
                            new CountryCode("GEO"),
                            new CountryCode("IRQ"),
                            new CountryCode("MAR"),
                            new CountryCode("TGO")
                    )
            ),
            new InfectionData(
                    "Yellow fever",
                    "Yellow fever is a viral infection transmitted by mosquitoes which predominantly feed between dawn and dusk, but may also bite at night, especially in the jungle environment. Symptoms may be absent or mild, but in severe cases, it can cause internal bleeding, organ failure and death.",
                    List.of(
                            new CountryCode("TGO")
                    )
            )
    );

    private record InfectionData(
            String infection,
            String description,
            List<CountryCode> countries
    ) {
        Risk toRisk() {
            return new Risk(infection, description);
        }
    }
}