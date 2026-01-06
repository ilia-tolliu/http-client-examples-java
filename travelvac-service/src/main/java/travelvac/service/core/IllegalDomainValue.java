package travelvac.service.core;

public class IllegalDomainValue extends RuntimeException {

    private final String expectedValueDescription;

    private final String actualValue;

    public IllegalDomainValue(String expectedValueDescription, String actualValue) {
        var message = "Expected %s but got [%s]"
                .formatted(expectedValueDescription, actualValue);
        super(message);

        this.actualValue = actualValue;
        this.expectedValueDescription = expectedValueDescription;
    }
}
