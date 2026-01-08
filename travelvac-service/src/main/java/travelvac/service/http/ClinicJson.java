package travelvac.service.http;

import travelvac.service.core.Clinic;

record ClinicJson(
    String clinicId,
    String countryCode,
    String city,
    String address,
    String description
) {
    static ClinicJson fromClinic(Clinic clinic) {
        return new ClinicJson(
                clinic.clinicId().format(),
                clinic.countryCode().alpha3(),
                clinic.city(),
                clinic.address(),
                clinic.description()
        );
    }
}

