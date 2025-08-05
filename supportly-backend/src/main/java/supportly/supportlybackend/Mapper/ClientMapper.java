package supportly.supportlybackend.Mapper;

import org.mapstruct.Mapper;
import supportly.supportlybackend.Dto.ClientDto;
import supportly.supportlybackend.Model.Client;

@Mapper(uses = CompanyMapper.class)
public interface ClientMapper {

    ClientDto toDto(Client client);
    Client toEntity(ClientDto clientDto);
}
