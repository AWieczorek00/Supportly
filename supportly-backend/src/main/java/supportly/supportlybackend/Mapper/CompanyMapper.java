package supportly.supportlybackend.Mapper;

import org.mapstruct.Mapper;
import supportly.supportlybackend.Dto.CompanyDto;
import supportly.supportlybackend.Model.Company;

@Mapper()
public interface CompanyMapper {

    CompanyDto toDto(Company company);
    Company toEntity(CompanyDto dto);
}
