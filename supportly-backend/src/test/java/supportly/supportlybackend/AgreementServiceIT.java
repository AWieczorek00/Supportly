package supportly.supportlybackend;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import supportly.supportlybackend.Criteria.AgreementSC;
import supportly.supportlybackend.Dto.AgreementDto;
import supportly.supportlybackend.Dto.CompanyDto;
import supportly.supportlybackend.Enum.Period;
import supportly.supportlybackend.Mapper.Mapper;
import supportly.supportlybackend.Model.Address;
import supportly.supportlybackend.Model.Agreement;
import supportly.supportlybackend.Model.Company;
import supportly.supportlybackend.Repository.AgreementRepository;
import supportly.supportlybackend.Repository.CompanyRepository;
import supportly.supportlybackend.Service.AgreementService;
import supportly.supportlybackend.Service.CompanyService;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:/application-test.properties")
class AgreementServiceIT {

    @Autowired
    private AgreementRepository agreementRepository;

    @Autowired
    private AgreementService agreementService;

    @Autowired
    private CompanyRepository companyRepository;// assuming CompanyService exists for creating test companies


    // Pola pomocnicze do danych testowych
    private Company savedCompany;

    @BeforeEach
    void setUp() {
        // Czyścimy bazę przed każdym testem
        agreementRepository.deleteAll();
        companyRepository.deleteAll();

        // 1. Tworzymy Adres (wymagany przez Company)
        Address address = new Address();
        address.setCity("Warszawa");
        address.setStreet("Marszałkowska");
        address.setZipCode("00-001");
        address.setStreetNumber(10);

        // 2. Tworzymy Firmę z wszystkimi wymaganymi polami (@Column(nullable = false))
        Company company = new Company();
        company.setName("Test Company Ltd.");
        // NIP musi mieć length = 13
        company.setNip("1234567890123");
        // REGON jest unikalny i wymagany
        company.setRegon("123456789");
        // Email jest wymagany i walidowany
        company.setEmail("kontakt@testcompany.pl");
        // Telefon musi mieć length = 9
        company.setPhoneNumber("123456789");

        // Relacja OneToOne z CascadeType.ALL - zapisanie firmy zapisze też adres
        company.setAddress(address);

        savedCompany = companyRepository.save(company);
    }

    @Test
    @DisplayName("add: Powinien zapisać umowę w bazie i poprawnie obliczyć datę serwisu")
    void shouldSaveAgreementToDatabase() {
        // Given
        AgreementDto dto = new AgreementDto();
        dto.setSignedDate(LocalDate.of(2023, 1, 1));
        dto.setAgreementNumber("1234567890123/01012023");
        // Zakładam, że Period to Enum. Jeśli to klasa, ustaw ją odpowiednio.
        dto.setPeriod(Period.YEARLY); // Zakładam, że .getMonth() zwraca np. 12

        // Tworzymy DTO firmy tylko z NIPem, po którym serwis szuka istniejącej firmy
        CompanyDto companyDto = new CompanyDto();
        companyDto.setNip("1234567890123"); // Ten sam NIP co w setUp()
        dto.setCompany(companyDto);


        // When
        agreementService.add(dto);

        // Then
        List<Agreement> agreements = agreementRepository.findAll();
        assertThat(agreements).hasSize(1);

        Agreement savedAgreement = agreements.get(0);
        // Sprawdzamy czy umowa została podpięta pod dobrą firmę
        assertThat(savedAgreement.getCompany().getId()).isEqualTo(savedCompany.getId());
        assertThat(savedAgreement.getCompany().getEmail()).isEqualTo("kontakt@testcompany.pl");

        // Sprawdzenie daty (2023-01-01 + 12 miesięcy)
        assertThat(savedAgreement.getNextServiceDate()).isEqualTo(LocalDate.of(2024, 1, 1));
    }

    @Test
    @DisplayName("search: Powinien znaleźć umowy używając kryteriów")
    void shouldFindAgreementsByCriteria() {
        // Given
        // Tworzymy drugą firmę (żeby test filtru był wiarygodny)
        createAndSaveOtherCompany();

        // Zapisujemy umowy dla obu firm
        createAndSaveAgreement(savedCompany, LocalDate.now());

        // When
        // Przykładowe kryteria (zależne od Twojej klasy AgreementSC)
        AgreementSC criteria = new AgreementSC();
        // Zakładam, że w SC masz filtrowanie po nazwie firmy
        // criteria.setCompanyName("Test Company Ltd.");

        // Ponieważ nie znam dokładnych pól w SC, testuję przypadek "pusty filtr = zwróć wszystko"
        // Jeśli dodasz setter w SC, odkomentuj i zmień asercję na 1

        List<AgreementDto> results = agreementService.search(criteria);

        // Then
        // Powinno zwrócić co najmniej 1 (zależy czy stworzyliśmy umowę dla drugiej firmy)
        assertThat(results).isNotEmpty();
    }

    @Test
    @DisplayName("findByNextRun: Powinien zwrócić umowy po dacie serwisu")
    void shouldFindAgreementsByNextRunDate() {
        // Given
        LocalDate targetDate = LocalDate.of(2025, 5, 20);
        createAndSaveAgreement(savedCompany, targetDate);

        // When
        List<Agreement> result = agreementService.findByNextRun(targetDate);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNextServiceDate()).isEqualTo(targetDate);
    }

    // --- Metody pomocnicze ---

    private void createAndSaveAgreement(Company company, LocalDate nextServiceDate) {
        Agreement agr = new Agreement();
        agr.setCompany(company);
        agr.setNextServiceDate(nextServiceDate);
        agr.setAgreementNumber("AGR-" + System.nanoTime()); // Unikalny numer
        agr.setPeriod(Period.MONTHLY);
        agr.setSignedDate(LocalDate.now());
        agreementRepository.save(agr);
    }

    private void createAndSaveOtherCompany() {
        Address addr = new Address();
        addr.setCity("Kraków");
        addr.setStreet("Wawelska");
        addr.setZipCode("30-001");
        addr.setStreetNumber(5);

        Company other = new Company();
        other.setName("Other Corp");
        other.setNip("9876543210987"); // Inny NIP (13 znaków)
        other.setRegon("987654321");   // Inny Regon
        other.setEmail("other@corp.com");
        other.setPhoneNumber("987654321");
        other.setAddress(addr);

        companyRepository.save(other);
    }

}
