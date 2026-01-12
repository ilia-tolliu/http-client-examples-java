package travelvac.client.monitoring.types;

public record Clinic(
  String clinicId,
  String countryCode,
  String city,
  String address,
  String description
) {
}
