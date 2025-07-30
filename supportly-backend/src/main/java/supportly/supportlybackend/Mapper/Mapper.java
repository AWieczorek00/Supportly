package supportly.supportlybackend.Mapper;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import supportly.supportlybackend.Dto.AgreementDto;
import supportly.supportlybackend.Dto.CompanyDto;
import supportly.supportlybackend.Model.Agreement;
import supportly.supportlybackend.Model.Company;

public class Mapper {

    private static final AgreementMapper agreementMapper = Mappers.getMapper(AgreementMapper.class);
    private static final CompanyMapper companyMapper = Mappers.getMapper(CompanyMapper.class);

    public static Agreement toEntity(AgreementDto agreementDto) {
        return agreementMapper.toEntity(agreementDto);
    }

    public static AgreementDto toDto(Agreement agreement) {
        return agreementMapper.toDto(agreement);
    }

    public static Company toEntity(CompanyDto companyDto) {
        return companyMapper.toEntity(companyDto);
    }

    public static CompanyDto toDto(Company company) {
        return companyMapper.toDto(company);
    }
}
