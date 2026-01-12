package travelvac.client.monitoring.types;

import java.util.List;

public record GetClinicsResponse(
  List<Clinic> clinics
) {
}
