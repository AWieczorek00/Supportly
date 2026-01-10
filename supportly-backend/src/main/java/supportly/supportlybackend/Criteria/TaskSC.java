package supportly.supportlybackend.Criteria;

import lombok.Getter;
import lombok.Setter;
import supportly.supportlybackend.Annotation.SpecField;
import supportly.supportlybackend.Model.Employee;
import supportly.supportlybackend.Model.Order;

@Getter
@Setter
public class TaskSC {

    private String name;
    @SpecField(path = "client.companys.name")
    private String companyName;
    @SpecField(path = "employee.firstName")
    private String firstName;
    @SpecField(path = "employee.lastName")
    private String lastName;
    private Boolean done;
}
