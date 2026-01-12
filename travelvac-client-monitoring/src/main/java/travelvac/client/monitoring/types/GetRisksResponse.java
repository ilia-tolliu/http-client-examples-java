package travelvac.client.monitoring.types;

import java.util.List;

public record GetRisksResponse(
  List<Risk> risks
) {
}
