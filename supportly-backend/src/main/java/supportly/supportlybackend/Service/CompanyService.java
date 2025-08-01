package supportly.supportlybackend.Service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import supportly.supportlybackend.Dto.CompanyDto;
import supportly.supportlybackend.Mapper.Mapper;
import supportly.supportlybackend.Model.Company;
import supportly.supportlybackend.Repository.CompanyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public void add(Company company) {
        companyRepository.save(company);
    }

    public List<CompanyDto> findAllByName(String name) {
        return companyRepository.findAllByNameContains(name)
                .stream()
                .map(Mapper::toDto)
                .collect(Collectors.toList());
    }

    public Company findByNip(String nip) {
        return companyRepository.findByNip(nip);
    }

}
