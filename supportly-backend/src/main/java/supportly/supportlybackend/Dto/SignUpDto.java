package supportly.supportlybackend.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import supportly.supportlybackend.Model.Employee;


import java.util.Set;

@Data
public class SignUpDto {

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 5, max = 40)
    private String password;

    @NotBlank
    private Employee employee;

    private Set<String> role;
}
