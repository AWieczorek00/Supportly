package supportly.supportlybackend.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class EmployeeDto {

    private String firstName;
    private String secondName;
    private String lastName;
    private String phoneNumber;
    private LocalDate dateOfCreation;


    public EmployeeDto(String firstName, String secondName, String lastName, String phoneNumber) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }
}
