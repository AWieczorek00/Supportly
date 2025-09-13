package supportly.supportlybackend.Service;

import jakarta.persistence.*;
import lombok.Data;
import supportly.supportlybackend.Dto.EmployeeDto;
import supportly.supportlybackend.Dto.OrderDto;
import supportly.supportlybackend.Model.Employee;

import java.time.LocalDate;
import java.util.List;

@Data
public class TaskDto {

    private Long id;

    private String name;

    private LocalDate executionTime;

    private Boolean done;

    private String email;

    private OrderDto order;

    private List<EmployeeDto> employees;

}

