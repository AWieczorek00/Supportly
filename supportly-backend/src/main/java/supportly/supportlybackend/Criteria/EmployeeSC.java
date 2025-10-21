package supportly.supportlybackend.Criteria;

import lombok.Getter;
import lombok.Setter;
import supportly.supportlybackend.Annotation.SpecField;
import supportly.supportlybackend.Enum.ERole;
import supportly.supportlybackend.Model.Role;

import static supportly.supportlybackend.Annotation.OperatorSql.LIKE;

@Getter
@Setter
public class EmployeeSC {

    @SpecField(path = "firstName", operator = LIKE)
    private String firstName;
    @SpecField(path = "lastName", operator = LIKE)
    private String lastName;
    @SpecField(path = "phoneNumber", operator = LIKE)
    private String phoneNumber;
}
