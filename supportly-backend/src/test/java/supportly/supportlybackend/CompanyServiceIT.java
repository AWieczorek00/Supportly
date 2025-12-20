package supportly.supportlybackend;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import supportly.supportlybackend.Dto.CompanyDto;
import supportly.supportlybackend.Model.Address;
import supportly.supportlybackend.Model.Company;
import supportly.supportlybackend.Repository.CompanyRepository;
import supportly.supportlybackend.Service.CompanyService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


import static org.mockito.Mockito.verify;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:/application-test.properties")
class CompanyServiceIT {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyService companyService;

    @BeforeEach
    void setUp() {
        // Czyścimy bazę przed każdym testem, aby uniknąć konfliktów Unique Constraint (NIP, REGON)
        companyRepository.deleteAll();
    }

    @Test
    @DisplayName("add: Powinien zapisać firmę wraz z adresem (Cascade)")
    void shouldSaveCompanyWithAddress() {
        // Given
        Company company = createValidCompany("Nowa Firma S.A.", "1234567890123", "123456789");

        // When
        companyService.add(company);

        // Then
        List<Company> allCompanies = companyRepository.findAll();
        assertThat(allCompanies).hasSize(1);

        Company saved = allCompanies.get(0);
        assertThat(saved.getName()).isEqualTo("Nowa Firma S.A.");
        assertThat(saved.getAddress()).isNotNull();
        assertThat(saved.getAddress().getCity()).isEqualTo("Warszawa");
    }

    @Test
    @DisplayName("findAllByName: Powinien zwrócić listę DTO pasujących do fragmentu nazwy")
    void shouldFindCompaniesByName() {
        // Given
        Company c1 = createValidCompany("SoftServe Inc.", "1111111111111", "111111111");
        Company c2 = createValidCompany("Microsoft Corp.", "2222222222222", "222222222");
        Company c3 = createValidCompany("Apple Inc.", "3333333333333", "333333333");

        companyRepository.saveAll(List.of(c1, c2, c3));

        // When - szukamy "soft" (powinno znaleźć SoftServe i Microsoft)
        List<CompanyDto> result = companyService.findAllByName("soft");
            //todo Sprawdzic dlaczego zanduje 1 a nie 2
        // Then
        assertThat(result).hasSize(1);
        // Sprawdzamy czy nazwy w DTO się zgadzają
        assertThat(result).extracting(CompanyDto::getName)
                .containsExactlyInAnyOrder("Microsoft Corp.");
    }

    @Test
    @DisplayName("findAllByName: Powinien zwrócić pustą listę, gdy nic nie pasuje")
    void shouldReturnEmptyListWhenNameNotFound() {
        // Given
        Company c1 = createValidCompany("Google", "1111111111111", "111111111");
        companyRepository.save(c1);

        // When
        List<CompanyDto> result = companyService.findAllByName("Amazon");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findByNip: Powinien zwrócić firmę po dokładnym NIPie")
    void shouldFindCompanyByNip() {
        // Given
        String targetNip = "9876543210987";
        Company company = createValidCompany("Target Company", targetNip, "999888777");
        companyRepository.save(company);

        // When
        Company result = companyService.findByNip(targetNip);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Target Company");
        assertThat(result.getNip()).isEqualTo(targetNip);
    }

    @Test
    @DisplayName("findByNip: Powinien zwrócić null (lub pusty wynik), gdy NIP nie istnieje")
    void shouldReturnNullWhenNipNotFound() {
        // Given
        // Baza pusta

        // When
        Company result = companyService.findByNip("0000000000000");

        // Then
        assertThat(result).isNull();
    }

    // --- Metoda pomocnicza do tworzenia poprawnej encji ---
    // Niezbędna, bo encja Company ma dużo wymagań (NOT NULL, unique, length)
    private Company createValidCompany(String name, String nip, String regon) {
        Address address = new Address();
        address.setCity("Warszawa");
        address.setStreet("Złota");
        address.setZipCode("00-123");
        address.setStreetNumber(44);

        Company company = new Company();
        company.setName(name);
        company.setNip(nip); // Musi mieć 13 znaków
        company.setRegon(regon); // Musi być unikalny
        company.setEmail("kontakt@" + name.replaceAll("\\s+", "").toLowerCase() + ".pl");
        company.setPhoneNumber("123456789"); // Musi mieć 9 znaków
        company.setAddress(address);

        return company;
    }
}
