package supportly.supportlybackend;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import supportly.supportlybackend.Enum.Period;
import supportly.supportlybackend.Model.Agreement;
import supportly.supportlybackend.Model.Company;
import supportly.supportlybackend.Repository.AgreementRepository;
import supportly.supportlybackend.Service.AgreementService;
import supportly.supportlybackend.Service.CompanyService;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:/application-test.properties")
class AgreementServiceTest {

    @Autowired
    private AgreementRepository agreementRepository;

    @Autowired
    private AgreementService agreementService;

    @Autowired
    private CompanyService companyService; // assuming CompanyService exists for creating test companies

    @Test
    void shouldAddAgreement() {
        // given
        Company company = new Company();
        company.setName("Test Company");
        company.setEmail("TestCompany@gmail.com");
        company.setPhoneNumber("123456789");
        company.setNip("1234567890");
        company.setRegon("1234567890");

        companyService.add(company); // add company to repository

        Agreement agreement = new Agreement();
        agreement.setCompany(company);
        agreement.setAgreementNumber("AG123");
        agreement.setSignedDate(LocalDate.of(2023, 1, 1));
        agreement.setPeriod(Period.MONTHLY);

        // when
//        agreementService.add(agreement);

        // then
        List<Agreement> agreements = agreementRepository.findAll();
        assertThat(agreements).hasSize(1);
        assertThat(agreements.get(0).getAgreementNumber()).isEqualTo("AG123");
    }

    @Test
    void shouldFindAllAgreements() {
        // given
        Company company = new Company();
        company.setName("Test Company");
        company.setEmail("TestCompany@gmail.com");
        company.setPhoneNumber("123456789");
        company.setNip("1234567890");
        company.setRegon("1234567890");

        companyService.add(company);

        Agreement agreement1 = new Agreement();
        agreement1.setCompany(company);
        agreement1.setAgreementNumber("AG123");
        agreement1.setSignedDate(LocalDate.of(2024, 1, 1));
        agreement1.setPeriod(Period.MONTHLY);

        Agreement agreement2 = new Agreement();
        agreement2.setCompany(company);
        agreement2.setAgreementNumber("AG124");
        agreement2.setSignedDate(LocalDate.of(2024, 2, 1));
        agreement2.setPeriod(Period.YEARLY);

//        agreementService.add(agreement1);
//        agreementService.add(agreement2);

        // when
//        List<Agreement> result = agreementService.findAll("Test");

        // then
//        assertThat(result).hasSize(1);
//        assertThat(result).extracting(Agreement::getAgreementNumber).containsExactlyInAnyOrder("AG123");
    }
}
