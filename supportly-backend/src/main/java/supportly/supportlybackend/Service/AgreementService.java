package supportly.supportlybackend.Service;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import supportly.supportlybackend.Criteria.AgreementSC;
import supportly.supportlybackend.Criteria.GenericSpecificationBuilder;
import supportly.supportlybackend.Dto.AgreementDto;
import supportly.supportlybackend.Mapper.Mapper;
import supportly.supportlybackend.Model.Agreement;
import supportly.supportlybackend.Model.Company;
import supportly.supportlybackend.Repository.AgreementRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AgreementService {

    private final AgreementRepository agreementRepository;
    private final CompanyService companyService;

    public void add(AgreementDto agreementDto) {
        Company company = companyService.findByNip(agreementDto.getCompany().getNip());
        Agreement agreement = Mapper.toEntity(agreementDto);
        agreement.setCompany(null);
        agreement.setNextServiceDate(agreementDto.getSignedDate().plusMonths(agreementDto.getPeriod().getMonth()));
        agreementRepository.save(agreement);
    }

    public List<AgreementDto> findAll() {
        return agreementRepository.findAll().stream().map(Mapper::toDto).collect(Collectors.toList());
    }


    public List<AgreementDto> search(AgreementSC criteria) {
        GenericSpecificationBuilder<Agreement> builder = new GenericSpecificationBuilder<>();
        Specification<Agreement> spec = builder.build(criteria);
        return agreementRepository.findAll(spec).stream().map(Mapper::toDto).collect(Collectors.toList());
    }

    public List<Agreement> findByNextRun(LocalDate nextRun) {
        return  agreementRepository.findAllByNextServiceDate(nextRun);
    }
}
