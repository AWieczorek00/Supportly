package supportly.supportlybackend.Dto;

import lombok.Data;

@Data
public class CompanyDto {

    private Long id;
    private String name;
    private String nip;
    private AddressDto address;
    private String phoneNumber;
    private String email;
    private String regon;
}
