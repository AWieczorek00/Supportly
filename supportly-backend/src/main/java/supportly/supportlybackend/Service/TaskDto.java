package supportly.supportlybackend.Service;

import jakarta.persistence.*;
import lombok.Data;
import supportly.supportlybackend.Model.Employee;

import java.time.LocalDate;

@Data
public class TaskDto {


    private String name;

    private LocalDate executionTime;

    private Boolean done;

    private String email;
}
