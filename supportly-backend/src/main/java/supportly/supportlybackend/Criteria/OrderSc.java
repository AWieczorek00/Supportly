package supportly.supportlybackend.Criteria;


import lombok.Getter;
import lombok.Setter;
import supportly.supportlybackend.Annotation.SpecField;

@Getter
@Setter
public class OrderSc {

    @SpecField(path = "client.company.name")
    private String nameCompany;

    @SpecField(path = "client.firstName")
    private String firstName;

    @SpecField(path = "client.lastName")
    private String lastName;

    @SpecField(path = "client.phoneNumber")
    private String phoneNumber;

    @SpecField(path = "client.email")
    private String email;

    @SpecField(path = "dateOfAdmission")
    private String dateOfAdmissionFrom;

    @SpecField(path = "dateOfAdmission")
    private String dateOfAdmissionTo;

    @SpecField(path = "dateOfExecution")
    private String dateOfExecutionFrom;

    @SpecField(path = "dateOfExecution")
    private Double dateOfExecutionTo;

    private Double status;

    private Double priority;
}
