package supportly.supportlybackend.Dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderDto {

    private Long id;
    private ClientDto client;
    private List<EmployeeDto> employees;
    private LocalDate dateOfAdmission;
    private LocalDate dateOfExecution;
    private String agreementNumber;
    private float manHour;
    private float distance;
    private String priority;
    private String status;
    private String period;
    private String note;


}
