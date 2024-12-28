package supportly.supportlybackend.Dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Setter
@Getter
public class EmailDto {
    @Email
    private String email;
    private String subject;
    private String text;
    private Boolean isHtmlContent;
}
