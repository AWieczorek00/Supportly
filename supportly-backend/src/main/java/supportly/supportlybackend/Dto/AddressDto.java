package supportly.supportlybackend.Dto;

import lombok.Data;

@Data
public class AddressDto {

    private Long id;
    private String street;
    private String city;
    private String zipCode;
    private String apartmentNumber;
    private String streetNumber;
}
