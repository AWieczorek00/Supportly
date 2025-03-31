package supportly.supportlybackend.Service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import supportly.supportlybackend.Model.Company;
import supportly.supportlybackend.Repository.CompanyRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public void add(Company company) {
        companyRepository.save(company);
    }

    public List<Company> findAllByName(String name) {
        return companyRepository.findAllByNameContains(name);
    }

}
