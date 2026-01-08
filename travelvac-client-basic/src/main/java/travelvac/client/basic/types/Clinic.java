package travelvac.client.basic.types;

public record Clinic(
  String clinicId,
  String countryCode,
  String city,
  String address,
  String description
) {
}
