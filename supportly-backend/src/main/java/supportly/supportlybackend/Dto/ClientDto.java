package supportly.supportlybackend.Dto;

import lombok.Data;

@Data
public class ClientDto {

    private Long id;
    private String firstName;
    private String LastName;
    private String phoneNumber;
    private String email;
    private CompanyDto company;
    private String type;
}
