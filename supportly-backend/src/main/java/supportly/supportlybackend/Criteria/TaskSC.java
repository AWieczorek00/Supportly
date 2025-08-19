package supportly.supportlybackend.Criteria;

import lombok.Getter;
import lombok.Setter;
import supportly.supportlybackend.Model.Employee;
import supportly.supportlybackend.Model.Order;

@Getter
@Setter
public class TaskSC {

    private String name;
    private Order order;
    private Employee employee;
    private Boolean done;
}
