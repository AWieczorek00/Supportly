package supportly.supportlybackend.Criteria;

import lombok.Getter;
import lombok.Setter;
import supportly.supportlybackend.Annotation.SpecField;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
public class AgreementSC {

    @SpecField(path = "company.name")
    private String name;
    @SpecField(path = "signedDate")
    private LocalDate signedDateFrom;
    @SpecField(path = "signedDate")
    private LocalDate signedDateTo;
    private Period period;
    @SpecField(path = "company.nip")
    private String nip;
    @SpecField(path = "company.email")
    private String email;

}
