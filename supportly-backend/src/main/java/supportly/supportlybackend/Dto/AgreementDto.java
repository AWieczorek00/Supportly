package supportly.supportlybackend.Dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import supportly.supportlybackend.Enum.Period;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgreementDto {

    private CompanyDto company;
    private String agreementNumber;
    private LocalDate signedDate;
    private Period period;

}
