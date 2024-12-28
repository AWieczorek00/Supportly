package supportly.supportlybackend.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;



@Data
public class LoginDto {
//    @NotBlank
//    private String username;
//    @NotBlank
//    private String password;

    private String email;

    private String password;
}
