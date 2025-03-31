package supportly.supportlybackend;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import supportly.supportlybackend.Model.Address;
import supportly.supportlybackend.Model.Company;
import supportly.supportlybackend.Repository.CompanyRepository;
import supportly.supportlybackend.Service.CompanyService;
import static org.assertj.core.api.Assertions.assertThat;


import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:/application-test.properties")
class CompanyServiceTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyService companyService;

    @Test
    void shouldAddCompany() {
        // given
        Company company = new Company();
        company.setName("Test Company");
        company.setEmail("TestCompany@gmail.com");
        company.setPhoneNumber("123456789");
        company.setNip("1234567890");
        company.setRegon("1234567890");
        Address address = new Address();
        address.setCity("Test City");
        address.setStreet("Test Street");
        address.setZipCode("12-345");
        address.setStreetNumber(12);


        company.setAddress(address);


        // when
        companyService.add(company);

        // then
        assertThat(companyRepository.findAll()).hasSize(1);
        assertThat(companyRepository.findAll().getFirst().getName()).isEqualTo("Test Company");
    }


    @Test
    void shouldFindAllByName() {
        // given
        Company company1 = new Company();
        company1.setName("Test Company A");
        company1.setEmail("testA@company.com");
        company1.setPhoneNumber("111222333");
        company1.setNip("9876543210");
        company1.setRegon("9876543210");
        Address address1 = new Address();
        address1.setCity("City A");
        address1.setStreet("Street A");
        address1.setZipCode("11-222");
        address1.setStreetNumber(10);
        company1.setAddress(address1);

        Company company2 = new Company();
        company2.setName("Test Company B");
        company2.setEmail("testB@company.com");
        company2.setPhoneNumber("444555666");
        company2.setNip("1122334455");
        company2.setRegon("1122334455");
        Address address2 = new Address();
        address2.setCity("City B");
        address2.setStreet("Street B");
        address2.setZipCode("22-333");
        address2.setStreetNumber(20);
        company2.setAddress(address2);

        companyRepository.save(company1);
        companyRepository.save(company2);

        // when
        var result = companyService.findAllByName("Test");

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Company::getName).containsExactlyInAnyOrder("Test Company A", "Test Company B");
    }
}