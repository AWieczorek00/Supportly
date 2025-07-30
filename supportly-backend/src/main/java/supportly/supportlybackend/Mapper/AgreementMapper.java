package supportly.supportlybackend.Mapper;

import org.mapstruct.Mapper;
import supportly.supportlybackend.Dto.AgreementDto;
import supportly.supportlybackend.Model.Agreement;

@Mapper(uses = CompanyMapper.class)
public interface AgreementMapper {

    AgreementDto toDto(Agreement agreement);
    Agreement toEntity(AgreementDto dto);
}
