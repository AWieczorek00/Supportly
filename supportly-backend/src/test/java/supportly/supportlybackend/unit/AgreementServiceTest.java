package supportly.supportlybackend.unit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import supportly.supportlybackend.Criteria.AgreementSC;
import supportly.supportlybackend.Dto.AgreementDto;
import supportly.supportlybackend.Dto.CompanyDto; // Zakładam istnienie
import supportly.supportlybackend.Mapper.Mapper;
import supportly.supportlybackend.Model.Agreement;
import supportly.supportlybackend.Model.Company;
import supportly.supportlybackend.Repository.AgreementRepository;
import supportly.supportlybackend.Enum.Period; // Zakładam istnienie Enuma lub klasy
import supportly.supportlybackend.Service.AgreementService;
import supportly.supportlybackend.Service.CompanyService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//sh "mvn verify -Dtest=**/supportlybackend/** -Dspring.profiles.active=posgres -Dmaven.test.failure.ignore=true"
//sh "mvn verify -Dtest=**/integration/** -Dspring.profiles.active=posgres -Dmaven.test.failure.ignore=true"
//sh "mvn verify -Dtest=**/unit/** -Dspring.profiles.active=posgres -Dmaven.test.failure.ignore=true"

@ExtendWith(MockitoExtension.class)
class AgreementServiceTest {

    @Mock
    private AgreementRepository agreementRepository;

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private AgreementService agreementService;

    private MockedStatic<Mapper> mapperMock;

    @Captor
    private ArgumentCaptor<Agreement> agreementCaptor;

    @BeforeEach
    void setUp() {
        mapperMock = Mockito.mockStatic(Mapper.class);
    }

    @AfterEach
    void tearDown() {
        mapperMock.close();
    }

    @Test
    @DisplayName("add: Powinien obliczyć datę serwisu, przypisać firmę i zapisać umowę")
    void add_ShouldCalculateDateAndLinkCompany_ThenSave() {
        // Given
        String nip = "1234567890";
        LocalDate signedDate = LocalDate.of(2023, 1, 15);
        int periodMonths = 12; // np. okres roczny

        // Tworzymy DTO
        CompanyDto companyDto = new CompanyDto();
        companyDto.setNip(nip);

        AgreementDto agreementDto = new AgreementDto();
        agreementDto.setCompany(companyDto);
        agreementDto.setSignedDate(signedDate);

        // Mockowanie obiektu Period (zakładam, że to Enum lub obiekt z metodą getMonth())
        // Tutaj symuluję, że getPeriod() zwraca obiekt, który ma metodę getMonth() zwracającą int
        Period periodMock = mock(Period.class);
        when(periodMock.getMonth()).thenReturn(periodMonths);
        agreementDto.setPeriod(periodMock);

        // Tworzymy encje
        Company companyEntity = new Company();
        companyEntity.setNip(nip);

        Agreement agreementEntity = new Agreement();
        // Obiekt czysty przed modyfikacjami w serwisie

        // Mockowanie zależności
        when(companyService.findByNip(nip)).thenReturn(companyEntity);
        mapperMock.when(() -> Mapper.toEntity(agreementDto)).thenReturn(agreementEntity);

        // When
        agreementService.add(agreementDto);

        // Then
        // Przechwytujemy to, co trafiło do save()
        verify(agreementRepository).save(agreementCaptor.capture());
        Agreement savedAgreement = agreementCaptor.getValue();

        // 1. Sprawdzamy czy przypisano firmę
        assertThat(savedAgreement.getCompany()).isEqualTo(companyEntity);

        // 2. Sprawdzamy kluczową logikę biznesową: czy data została obliczona poprawnie
        // signedDate (2023-01-15) + 12 miesięcy = 2024-01-15
        LocalDate expectedDate = signedDate.plusMonths(periodMonths);
        assertThat(savedAgreement.getNextServiceDate()).isEqualTo(expectedDate);
    }

    @Test
    @DisplayName("add: Powinien obliczyć datę serwisu, przypisać firmę i zapisać umowę")
    void add_ShouldCalculateDateAndLinkCompany_ThenSave2() {
        // Given
        String nip = "1234567890";
        LocalDate signedDate = LocalDate.of(2023, 1, 15);
        int periodMonths = 12; // np. okres roczny

        // Tworzymy DTO
        CompanyDto companyDto = new CompanyDto();
        companyDto.setNip(nip);

        AgreementDto agreementDto = new AgreementDto();
        agreementDto.setCompany(companyDto);
        agreementDto.setSignedDate(signedDate);

        // Mockowanie obiektu Period (zakładam, że to Enum lub obiekt z metodą getMonth())
        // Tutaj symuluję, że getPeriod() zwraca obiekt, który ma metodę getMonth() zwracającą int
        Period periodMock = mock(Period.class);
        when(periodMock.getMonth()).thenReturn(periodMonths);
        agreementDto.setPeriod(periodMock);

        // Tworzymy encje
        Company companyEntity = new Company();
        companyEntity.setNip(nip);

        Agreement agreementEntity = new Agreement();
        // Obiekt czysty przed modyfikacjami w serwisie

        // Mockowanie zależności
        when(companyService.findByNip(nip)).thenReturn(companyEntity);
        mapperMock.when(() -> Mapper.toEntity(agreementDto)).thenReturn(agreementEntity);

        // When
        agreementService.add(agreementDto);

        // Then
        // Przechwytujemy to, co trafiło do save()
        verify(agreementRepository).save(agreementCaptor.capture());
        Agreement savedAgreement = agreementCaptor.getValue();

        // 1. Sprawdzamy czy przypisano firmę
        assertThat(savedAgreement.getCompany()).isEqualTo(companyEntity);

        // 2. Sprawdzamy kluczową logikę biznesową: czy data została obliczona poprawnie
        // signedDate (2023-01-15) + 12 miesięcy = 2024-01-15
        LocalDate expectedDate = signedDate.plusMonths(periodMonths);
        assertThat(savedAgreement.getNextServiceDate()).isEqualTo(expectedDate);
    }

    @Test
    @DisplayName("add: Powinien obliczyć datę serwisu, przypisać firmę i zapisać umowę")
    void add_ShouldCalculateDateAndLinkCompany_ThenSave3() {
        // Given
        String nip = "1234567890";
        LocalDate signedDate = LocalDate.of(2023, 1, 15);
        int periodMonths = 12; // np. okres roczny

        // Tworzymy DTO
        CompanyDto companyDto = new CompanyDto();
        companyDto.setNip(nip);

        AgreementDto agreementDto = new AgreementDto();
        agreementDto.setCompany(companyDto);
        agreementDto.setSignedDate(signedDate);

        // Mockowanie obiektu Period (zakładam, że to Enum lub obiekt z metodą getMonth())
        // Tutaj symuluję, że getPeriod() zwraca obiekt, który ma metodę getMonth() zwracającą int
        Period periodMock = mock(Period.class);
        when(periodMock.getMonth()).thenReturn(periodMonths);
        agreementDto.setPeriod(periodMock);

        // Tworzymy encje
        Company companyEntity = new Company();
        companyEntity.setNip(nip);

        Agreement agreementEntity = new Agreement();
        // Obiekt czysty przed modyfikacjami w serwisie

        // Mockowanie zależności
        when(companyService.findByNip(nip)).thenReturn(companyEntity);
        mapperMock.when(() -> Mapper.toEntity(agreementDto)).thenReturn(agreementEntity);

        // When
        agreementService.add(agreementDto);

        // Then
        // Przechwytujemy to, co trafiło do save()
        verify(agreementRepository).save(agreementCaptor.capture());
        Agreement savedAgreement = agreementCaptor.getValue();

        // 1. Sprawdzamy czy przypisano firmę
        assertThat(savedAgreement.getCompany()).isEqualTo(companyEntity);

        // 2. Sprawdzamy kluczową logikę biznesową: czy data została obliczona poprawnie
        // signedDate (2023-01-15) + 12 miesięcy = 2024-01-15
        LocalDate expectedDate = signedDate.plusMonths(periodMonths);
        assertThat(savedAgreement.getNextServiceDate()).isEqualTo(expectedDate);
    }

    @Test
    @DisplayName("add: Powinien obliczyć datę serwisu, przypisać firmę i zapisać umowę")
    void add_ShouldCalculateDateAndLinkCompany_ThenSave4() {
        // Given
        String nip = "1234567890";
        LocalDate signedDate = LocalDate.of(2023, 1, 15);
        int periodMonths = 12; // np. okres roczny

        // Tworzymy DTO
        CompanyDto companyDto = new CompanyDto();
        companyDto.setNip(nip);

        AgreementDto agreementDto = new AgreementDto();
        agreementDto.setCompany(companyDto);
        agreementDto.setSignedDate(signedDate);

        // Mockowanie obiektu Period (zakładam, że to Enum lub obiekt z metodą getMonth())
        // Tutaj symuluję, że getPeriod() zwraca obiekt, który ma metodę getMonth() zwracającą int
        Period periodMock = mock(Period.class);
        when(periodMock.getMonth()).thenReturn(periodMonths);
        agreementDto.setPeriod(periodMock);

        // Tworzymy encje
        Company companyEntity = new Company();
        companyEntity.setNip(nip);

        Agreement agreementEntity = new Agreement();
        // Obiekt czysty przed modyfikacjami w serwisie

        // Mockowanie zależności
        when(companyService.findByNip(nip)).thenReturn(companyEntity);
        mapperMock.when(() -> Mapper.toEntity(agreementDto)).thenReturn(agreementEntity);

        // When
        agreementService.add(agreementDto);

        // Then
        // Przechwytujemy to, co trafiło do save()
        verify(agreementRepository).save(agreementCaptor.capture());
        Agreement savedAgreement = agreementCaptor.getValue();

        // 1. Sprawdzamy czy przypisano firmę
        assertThat(savedAgreement.getCompany()).isEqualTo(companyEntity);

        // 2. Sprawdzamy kluczową logikę biznesową: czy data została obliczona poprawnie
        // signedDate (2023-01-15) + 12 miesięcy = 2024-01-15
        LocalDate expectedDate = signedDate.plusMonths(periodMonths);
        assertThat(savedAgreement.getNextServiceDate()).isEqualTo(expectedDate);
    }

    @Test
    @DisplayName("add: Powinien obliczyć datę serwisu, przypisać firmę i zapisać umowę")
    void add_ShouldCalculateDateAndLinkCompany_ThenSave5() {
        // Given
        String nip = "1234567890";
        LocalDate signedDate = LocalDate.of(2023, 1, 15);
        int periodMonths = 12; // np. okres roczny

        // Tworzymy DTO
        CompanyDto companyDto = new CompanyDto();
        companyDto.setNip(nip);

        AgreementDto agreementDto = new AgreementDto();
        agreementDto.setCompany(companyDto);
        agreementDto.setSignedDate(signedDate);

        // Mockowanie obiektu Period (zakładam, że to Enum lub obiekt z metodą getMonth())
        // Tutaj symuluję, że getPeriod() zwraca obiekt, który ma metodę getMonth() zwracającą int
        Period periodMock = mock(Period.class);
        when(periodMock.getMonth()).thenReturn(periodMonths);
        agreementDto.setPeriod(periodMock);

        // Tworzymy encje
        Company companyEntity = new Company();
        companyEntity.setNip(nip);

        Agreement agreementEntity = new Agreement();
        // Obiekt czysty przed modyfikacjami w serwisie

        // Mockowanie zależności
        when(companyService.findByNip(nip)).thenReturn(companyEntity);
        mapperMock.when(() -> Mapper.toEntity(agreementDto)).thenReturn(agreementEntity);

        // When
        agreementService.add(agreementDto);

        // Then
        // Przechwytujemy to, co trafiło do save()
        verify(agreementRepository).save(agreementCaptor.capture());
        Agreement savedAgreement = agreementCaptor.getValue();

        // 1. Sprawdzamy czy przypisano firmę
        assertThat(savedAgreement.getCompany()).isEqualTo(companyEntity);

        // 2. Sprawdzamy kluczową logikę biznesową: czy data została obliczona poprawnie
        // signedDate (2023-01-15) + 12 miesięcy = 2024-01-15
        LocalDate expectedDate = signedDate.plusMonths(periodMonths);
        assertThat(savedAgreement.getNextServiceDate()).isEqualTo(expectedDate);
    }

    @Test
    @DisplayName("add: Powinien obliczyć datę serwisu, przypisać firmę i zapisać umowę")
    void add_ShouldCalculateDateAndLinkCompany_ThenSave6() {
        // Given
        String nip = "1234567890";
        LocalDate signedDate = LocalDate.of(2023, 1, 15);
        int periodMonths = 12; // np. okres roczny

        // Tworzymy DTO
        CompanyDto companyDto = new CompanyDto();
        companyDto.setNip(nip);

        AgreementDto agreementDto = new AgreementDto();
        agreementDto.setCompany(companyDto);
        agreementDto.setSignedDate(signedDate);

        // Mockowanie obiektu Period (zakładam, że to Enum lub obiekt z metodą getMonth())
        // Tutaj symuluję, że getPeriod() zwraca obiekt, który ma metodę getMonth() zwracającą int
        Period periodMock = mock(Period.class);
        when(periodMock.getMonth()).thenReturn(periodMonths);
        agreementDto.setPeriod(periodMock);

        // Tworzymy encje
        Company companyEntity = new Company();
        companyEntity.setNip(nip);

        Agreement agreementEntity = new Agreement();
        // Obiekt czysty przed modyfikacjami w serwisie

        // Mockowanie zależności
        when(companyService.findByNip(nip)).thenReturn(companyEntity);
        mapperMock.when(() -> Mapper.toEntity(agreementDto)).thenReturn(agreementEntity);

        // When
        agreementService.add(agreementDto);

        // Then
        // Przechwytujemy to, co trafiło do save()
        verify(agreementRepository).save(agreementCaptor.capture());
        Agreement savedAgreement = agreementCaptor.getValue();

        // 1. Sprawdzamy czy przypisano firmę
        assertThat(savedAgreement.getCompany()).isEqualTo(companyEntity);

        // 2. Sprawdzamy kluczową logikę biznesową: czy data została obliczona poprawnie
        // signedDate (2023-01-15) + 12 miesięcy = 2024-01-15
        LocalDate expectedDate = signedDate.plusMonths(periodMonths);
        assertThat(savedAgreement.getNextServiceDate()).isEqualTo(expectedDate);
    }

    @Test
    @DisplayName("add: Powinien obliczyć datę serwisu, przypisać firmę i zapisać umowę")
    void add_ShouldCalculateDateAndLinkCompany_ThenSave7() {
        // Given
        String nip = "1234567890";
        LocalDate signedDate = LocalDate.of(2023, 1, 15);
        int periodMonths = 12; // np. okres roczny

        // Tworzymy DTO
        CompanyDto companyDto = new CompanyDto();
        companyDto.setNip(nip);

        AgreementDto agreementDto = new AgreementDto();
        agreementDto.setCompany(companyDto);
        agreementDto.setSignedDate(signedDate);

        // Mockowanie obiektu Period (zakładam, że to Enum lub obiekt z metodą getMonth())
        // Tutaj symuluję, że getPeriod() zwraca obiekt, który ma metodę getMonth() zwracającą int
        Period periodMock = mock(Period.class);
        when(periodMock.getMonth()).thenReturn(periodMonths);
        agreementDto.setPeriod(periodMock);

        // Tworzymy encje
        Company companyEntity = new Company();
        companyEntity.setNip(nip);

        Agreement agreementEntity = new Agreement();
        // Obiekt czysty przed modyfikacjami w serwisie

        // Mockowanie zależności
        when(companyService.findByNip(nip)).thenReturn(companyEntity);
        mapperMock.when(() -> Mapper.toEntity(agreementDto)).thenReturn(agreementEntity);

        // When
        agreementService.add(agreementDto);

        // Then
        // Przechwytujemy to, co trafiło do save()
        verify(agreementRepository).save(agreementCaptor.capture());
        Agreement savedAgreement = agreementCaptor.getValue();

        // 1. Sprawdzamy czy przypisano firmę
        assertThat(savedAgreement.getCompany()).isEqualTo(companyEntity);

        // 2. Sprawdzamy kluczową logikę biznesową: czy data została obliczona poprawnie
        // signedDate (2023-01-15) + 12 miesięcy = 2024-01-15
        LocalDate expectedDate = signedDate.plusMonths(periodMonths);
        assertThat(savedAgreement.getNextServiceDate()).isEqualTo(expectedDate);
    }

    @Test
    @DisplayName("add: Powinien obliczyć datę serwisu, przypisać firmę i zapisać umowę")
    void add_ShouldCalculateDateAndLinkCompany_ThenSave8() {
        // Given
        String nip = "1234567890";
        LocalDate signedDate = LocalDate.of(2023, 1, 15);
        int periodMonths = 12; // np. okres roczny

        // Tworzymy DTO
        CompanyDto companyDto = new CompanyDto();
        companyDto.setNip(nip);

        AgreementDto agreementDto = new AgreementDto();
        agreementDto.setCompany(companyDto);
        agreementDto.setSignedDate(signedDate);

        // Mockowanie obiektu Period (zakładam, że to Enum lub obiekt z metodą getMonth())
        // Tutaj symuluję, że getPeriod() zwraca obiekt, który ma metodę getMonth() zwracającą int
        Period periodMock = mock(Period.class);
        when(periodMock.getMonth()).thenReturn(periodMonths);
        agreementDto.setPeriod(periodMock);

        // Tworzymy encje
        Company companyEntity = new Company();
        companyEntity.setNip(nip);

        Agreement agreementEntity = new Agreement();
        // Obiekt czysty przed modyfikacjami w serwisie

        // Mockowanie zależności
        when(companyService.findByNip(nip)).thenReturn(companyEntity);
        mapperMock.when(() -> Mapper.toEntity(agreementDto)).thenReturn(agreementEntity);

        // When
        agreementService.add(agreementDto);

        // Then
        // Przechwytujemy to, co trafiło do save()
        verify(agreementRepository).save(agreementCaptor.capture());
        Agreement savedAgreement = agreementCaptor.getValue();

        // 1. Sprawdzamy czy przypisano firmę
        assertThat(savedAgreement.getCompany()).isEqualTo(companyEntity);

        // 2. Sprawdzamy kluczową logikę biznesową: czy data została obliczona poprawnie
        // signedDate (2023-01-15) + 12 miesięcy = 2024-01-15
        LocalDate expectedDate = signedDate.plusMonths(periodMonths);
        assertThat(savedAgreement.getNextServiceDate()).isEqualTo(expectedDate);
    }

    @Test
    @DisplayName("add: Powinien obliczyć datę serwisu, przypisać firmę i zapisać umowę")
    void add_ShouldCalculateDateAndLinkCompany_ThenSave9() {
        // Given
        String nip = "1234567890";
        LocalDate signedDate = LocalDate.of(2023, 1, 15);
        int periodMonths = 12; // np. okres roczny

        // Tworzymy DTO
        CompanyDto companyDto = new CompanyDto();
        companyDto.setNip(nip);

        AgreementDto agreementDto = new AgreementDto();
        agreementDto.setCompany(companyDto);
        agreementDto.setSignedDate(signedDate);

        // Mockowanie obiektu Period (zakładam, że to Enum lub obiekt z metodą getMonth())
        // Tutaj symuluję, że getPeriod() zwraca obiekt, który ma metodę getMonth() zwracającą int
        Period periodMock = mock(Period.class);
        when(periodMock.getMonth()).thenReturn(periodMonths);
        agreementDto.setPeriod(periodMock);

        // Tworzymy encje
        Company companyEntity = new Company();
        companyEntity.setNip(nip);

        Agreement agreementEntity = new Agreement();
        // Obiekt czysty przed modyfikacjami w serwisie

        // Mockowanie zależności
        when(companyService.findByNip(nip)).thenReturn(companyEntity);
        mapperMock.when(() -> Mapper.toEntity(agreementDto)).thenReturn(agreementEntity);

        // When
        agreementService.add(agreementDto);

        // Then
        // Przechwytujemy to, co trafiło do save()
        verify(agreementRepository).save(agreementCaptor.capture());
        Agreement savedAgreement = agreementCaptor.getValue();

        // 1. Sprawdzamy czy przypisano firmę
        assertThat(savedAgreement.getCompany()).isEqualTo(companyEntity);

        // 2. Sprawdzamy kluczową logikę biznesową: czy data została obliczona poprawnie
        // signedDate (2023-01-15) + 12 miesięcy = 2024-01-15
        LocalDate expectedDate = signedDate.plusMonths(periodMonths);
        assertThat(savedAgreement.getNextServiceDate()).isEqualTo(expectedDate);
    }

    @Test
    @DisplayName("add: Powinien obliczyć datę serwisu, przypisać firmę i zapisać umowę")
    void add_ShouldCalculateDateAndLinkCompany_ThenSave10() {
        // Given
        String nip = "1234567890";
        LocalDate signedDate = LocalDate.of(2023, 1, 15);
        int periodMonths = 12; // np. okres roczny

        // Tworzymy DTO
        CompanyDto companyDto = new CompanyDto();
        companyDto.setNip(nip);

        AgreementDto agreementDto = new AgreementDto();
        agreementDto.setCompany(companyDto);
        agreementDto.setSignedDate(signedDate);

        // Mockowanie obiektu Period (zakładam, że to Enum lub obiekt z metodą getMonth())
        // Tutaj symuluję, że getPeriod() zwraca obiekt, który ma metodę getMonth() zwracającą int
        Period periodMock = mock(Period.class);
        when(periodMock.getMonth()).thenReturn(periodMonths);
        agreementDto.setPeriod(periodMock);

        // Tworzymy encje
        Company companyEntity = new Company();
        companyEntity.setNip(nip);

        Agreement agreementEntity = new Agreement();
        // Obiekt czysty przed modyfikacjami w serwisie

        // Mockowanie zależności
        when(companyService.findByNip(nip)).thenReturn(companyEntity);
        mapperMock.when(() -> Mapper.toEntity(agreementDto)).thenReturn(agreementEntity);

        // When
        agreementService.add(agreementDto);

        // Then
        // Przechwytujemy to, co trafiło do save()
        verify(agreementRepository).save(agreementCaptor.capture());
        Agreement savedAgreement = agreementCaptor.getValue();

        // 1. Sprawdzamy czy przypisano firmę
        assertThat(savedAgreement.getCompany()).isEqualTo(companyEntity);

        // 2. Sprawdzamy kluczową logikę biznesową: czy data została obliczona poprawnie
        // signedDate (2023-01-15) + 12 miesięcy = 2024-01-15
        LocalDate expectedDate = signedDate.plusMonths(periodMonths);
        assertThat(savedAgreement.getNextServiceDate()).isEqualTo(expectedDate);
    }

    @Test
    @DisplayName("add: Powinien obliczyć datę serwisu, przypisać firmę i zapisać umowę")
    void add_ShouldCalculateDateAndLinkCompany_ThenSave11() {
        // Given
        String nip = "1234567890";
        LocalDate signedDate = LocalDate.of(2023, 1, 15);
        int periodMonths = 12; // np. okres roczny

        // Tworzymy DTO
        CompanyDto companyDto = new CompanyDto();
        companyDto.setNip(nip);

        AgreementDto agreementDto = new AgreementDto();
        agreementDto.setCompany(companyDto);
        agreementDto.setSignedDate(signedDate);

        // Mockowanie obiektu Period (zakładam, że to Enum lub obiekt z metodą getMonth())
        // Tutaj symuluję, że getPeriod() zwraca obiekt, który ma metodę getMonth() zwracającą int
        Period periodMock = mock(Period.class);
        when(periodMock.getMonth()).thenReturn(periodMonths);
        agreementDto.setPeriod(periodMock);

        // Tworzymy encje
        Company companyEntity = new Company();
        companyEntity.setNip(nip);

        Agreement agreementEntity = new Agreement();
        // Obiekt czysty przed modyfikacjami w serwisie

        // Mockowanie zależności
        when(companyService.findByNip(nip)).thenReturn(companyEntity);
        mapperMock.when(() -> Mapper.toEntity(agreementDto)).thenReturn(agreementEntity);

        // When
        agreementService.add(agreementDto);

        // Then
        // Przechwytujemy to, co trafiło do save()
        verify(agreementRepository).save(agreementCaptor.capture());
        Agreement savedAgreement = agreementCaptor.getValue();

        // 1. Sprawdzamy czy przypisano firmę
        assertThat(savedAgreement.getCompany()).isEqualTo(companyEntity);

        // 2. Sprawdzamy kluczową logikę biznesową: czy data została obliczona poprawnie
        // signedDate (2023-01-15) + 12 miesięcy = 2024-01-15
        LocalDate expectedDate = signedDate.plusMonths(periodMonths);
        assertThat(savedAgreement.getNextServiceDate()).isEqualTo(expectedDate);
    }

    @Test
    @DisplayName("add: Powinien obliczyć datę serwisu, przypisać firmę i zapisać umowę")
    void add_ShouldCalculateDateAndLinkCompany_ThenSave12() {
        // Given
        String nip = "1234567890";
        LocalDate signedDate = LocalDate.of(2023, 1, 15);
        int periodMonths = 12; // np. okres roczny

        // Tworzymy DTO
        CompanyDto companyDto = new CompanyDto();
        companyDto.setNip(nip);

        AgreementDto agreementDto = new AgreementDto();
        agreementDto.setCompany(companyDto);
        agreementDto.setSignedDate(signedDate);

        // Mockowanie obiektu Period (zakładam, że to Enum lub obiekt z metodą getMonth())
        // Tutaj symuluję, że getPeriod() zwraca obiekt, który ma metodę getMonth() zwracającą int
        Period periodMock = mock(Period.class);
        when(periodMock.getMonth()).thenReturn(periodMonths);
        agreementDto.setPeriod(periodMock);

        // Tworzymy encje
        Company companyEntity = new Company();
        companyEntity.setNip(nip);

        Agreement agreementEntity = new Agreement();
        // Obiekt czysty przed modyfikacjami w serwisie

        // Mockowanie zależności
        when(companyService.findByNip(nip)).thenReturn(companyEntity);
        mapperMock.when(() -> Mapper.toEntity(agreementDto)).thenReturn(agreementEntity);

        // When
        agreementService.add(agreementDto);

        // Then
        // Przechwytujemy to, co trafiło do save()
        verify(agreementRepository).save(agreementCaptor.capture());
        Agreement savedAgreement = agreementCaptor.getValue();

        // 1. Sprawdzamy czy przypisano firmę
        assertThat(savedAgreement.getCompany()).isEqualTo(companyEntity);

        // 2. Sprawdzamy kluczową logikę biznesową: czy data została obliczona poprawnie
        // signedDate (2023-01-15) + 12 miesięcy = 2024-01-15
        LocalDate expectedDate = signedDate.plusMonths(periodMonths);
        assertThat(savedAgreement.getNextServiceDate()).isEqualTo(expectedDate);
    }

    @Test
    @DisplayName("add: Powinien obliczyć datę serwisu, przypisać firmę i zapisać umowę")
    void add_ShouldCalculateDateAndLinkCompany_ThenSave13() {
        // Given
        String nip = "1234567890";
        LocalDate signedDate = LocalDate.of(2023, 1, 15);
        int periodMonths = 12; // np. okres roczny

        // Tworzymy DTO
        CompanyDto companyDto = new CompanyDto();
        companyDto.setNip(nip);

        AgreementDto agreementDto = new AgreementDto();
        agreementDto.setCompany(companyDto);
        agreementDto.setSignedDate(signedDate);

        // Mockowanie obiektu Period (zakładam, że to Enum lub obiekt z metodą getMonth())
        // Tutaj symuluję, że getPeriod() zwraca obiekt, który ma metodę getMonth() zwracającą int
        Period periodMock = mock(Period.class);
        when(periodMock.getMonth()).thenReturn(periodMonths);
        agreementDto.setPeriod(periodMock);

        // Tworzymy encje
        Company companyEntity = new Company();
        companyEntity.setNip(nip);

        Agreement agreementEntity = new Agreement();
        // Obiekt czysty przed modyfikacjami w serwisie

        // Mockowanie zależności
        when(companyService.findByNip(nip)).thenReturn(companyEntity);
        mapperMock.when(() -> Mapper.toEntity(agreementDto)).thenReturn(agreementEntity);

        // When
        agreementService.add(agreementDto);

        // Then
        // Przechwytujemy to, co trafiło do save()
        verify(agreementRepository).save(agreementCaptor.capture());
        Agreement savedAgreement = agreementCaptor.getValue();

        // 1. Sprawdzamy czy przypisano firmę
        assertThat(savedAgreement.getCompany()).isEqualTo(companyEntity);

        // 2. Sprawdzamy kluczową logikę biznesową: czy data została obliczona poprawnie
        // signedDate (2023-01-15) + 12 miesięcy = 2024-01-15
        LocalDate expectedDate = signedDate.plusMonths(periodMonths);
        assertThat(savedAgreement.getNextServiceDate()).isEqualTo(expectedDate);
    }

    @Test
    @DisplayName("add: Powinien obliczyć datę serwisu, przypisać firmę i zapisać umowę")
    void add_ShouldCalculateDateAndLinkCompany_ThenSave14() {
        // Given
        String nip = "1234567890";
        LocalDate signedDate = LocalDate.of(2023, 1, 15);
        int periodMonths = 12; // np. okres roczny

        // Tworzymy DTO
        CompanyDto companyDto = new CompanyDto();
        companyDto.setNip(nip);

        AgreementDto agreementDto = new AgreementDto();
        agreementDto.setCompany(companyDto);
        agreementDto.setSignedDate(signedDate);

        // Mockowanie obiektu Period (zakładam, że to Enum lub obiekt z metodą getMonth())
        // Tutaj symuluję, że getPeriod() zwraca obiekt, który ma metodę getMonth() zwracającą int
        Period periodMock = mock(Period.class);
        when(periodMock.getMonth()).thenReturn(periodMonths);
        agreementDto.setPeriod(periodMock);

        // Tworzymy encje
        Company companyEntity = new Company();
        companyEntity.setNip(nip);

        Agreement agreementEntity = new Agreement();
        // Obiekt czysty przed modyfikacjami w serwisie

        // Mockowanie zależności
        when(companyService.findByNip(nip)).thenReturn(companyEntity);
        mapperMock.when(() -> Mapper.toEntity(agreementDto)).thenReturn(agreementEntity);

        // When
        agreementService.add(agreementDto);

        // Then
        // Przechwytujemy to, co trafiło do save()
        verify(agreementRepository).save(agreementCaptor.capture());
        Agreement savedAgreement = agreementCaptor.getValue();

        // 1. Sprawdzamy czy przypisano firmę
        assertThat(savedAgreement.getCompany()).isEqualTo(companyEntity);

        // 2. Sprawdzamy kluczową logikę biznesową: czy data została obliczona poprawnie
        // signedDate (2023-01-15) + 12 miesięcy = 2024-01-15
        LocalDate expectedDate = signedDate.plusMonths(periodMonths);
        assertThat(savedAgreement.getNextServiceDate()).isEqualTo(expectedDate);
    }
//    mvn surefire:test -Dtest=*.unit.* -Pposgres -Dspring.profiles.active=posgres -Dmaven.test.failure.ignore=true
    @Test
    @DisplayName("add: Powinien obliczyć datę serwisu, przypisać firmę i zapisać umowę")
    void add_ShouldCalculateDateAndLinkCompany_ThenSave15() throws InterruptedException {
        Thread.sleep(15);
        // Given
        String nip = "1234567890";
        LocalDate signedDate = LocalDate.of(2023, 1, 15);
        int periodMonths = 12; // np. okres roczny

        // Tworzymy DTO
        CompanyDto companyDto = new CompanyDto();
        companyDto.setNip(nip);

        AgreementDto agreementDto = new AgreementDto();
        agreementDto.setCompany(companyDto);
        agreementDto.setSignedDate(signedDate);

        // Mockowanie obiektu Period (zakładam, że to Enum lub obiekt z metodą getMonth())
        // Tutaj symuluję, że getPeriod() zwraca obiekt, który ma metodę getMonth() zwracającą int
        Period periodMock = mock(Period.class);
        when(periodMock.getMonth()).thenReturn(periodMonths);
        agreementDto.setPeriod(periodMock);

        // Tworzymy encje
        Company companyEntity = new Company();
        companyEntity.setNip(nip);

        Agreement agreementEntity = new Agreement();
        // Obiekt czysty przed modyfikacjami w serwisie

        // Mockowanie zależności
        when(companyService.findByNip(nip)).thenReturn(companyEntity);
        mapperMock.when(() -> Mapper.toEntity(agreementDto)).thenReturn(agreementEntity);

        // When
        agreementService.add(agreementDto);

        // Then
        // Przechwytujemy to, co trafiło do save()
        verify(agreementRepository).save(agreementCaptor.capture());
        Agreement savedAgreement = agreementCaptor.getValue();

        // 1. Sprawdzamy czy przypisano firmę
        assertThat(savedAgreement.getCompany()).isEqualTo(companyEntity);

        // 2. Sprawdzamy kluczową logikę biznesową: czy data została obliczona poprawnie
        // signedDate (2023-01-15) + 12 miesięcy = 2024-01-15
        LocalDate expectedDate = signedDate.plusMonths(periodMonths);
        assertThat(savedAgreement.getNextServiceDate()).isEqualTo(expectedDate);
    }

    @Test
    @DisplayName("add: Powinien obliczyć datę serwisu, przypisać firmę i zapisać umowę")
    void add_ShouldCalculateDateAndLinkCompany_ThenSave16() throws InterruptedException {
        Thread.sleep(10);
        // Given
        String nip = "1234567890";
        LocalDate signedDate = LocalDate.of(2023, 1, 15);
        int periodMonths = 12; // np. okres roczny

        // Tworzymy DTO
        CompanyDto companyDto = new CompanyDto();
        companyDto.setNip(nip);

        AgreementDto agreementDto = new AgreementDto();
        agreementDto.setCompany(companyDto);
        agreementDto.setSignedDate(signedDate);

        // Mockowanie obiektu Period (zakładam, że to Enum lub obiekt z metodą getMonth())
        // Tutaj symuluję, że getPeriod() zwraca obiekt, który ma metodę getMonth() zwracającą int
        Period periodMock = mock(Period.class);
        when(periodMock.getMonth()).thenReturn(periodMonths);
        agreementDto.setPeriod(periodMock);

        // Tworzymy encje
        Company companyEntity = new Company();
        companyEntity.setNip(nip);

        Agreement agreementEntity = new Agreement();
        // Obiekt czysty przed modyfikacjami w serwisie

        // Mockowanie zależności
        when(companyService.findByNip(nip)).thenReturn(companyEntity);
        mapperMock.when(() -> Mapper.toEntity(agreementDto)).thenReturn(agreementEntity);

        // When
        agreementService.add(agreementDto);

        // Then
        // Przechwytujemy to, co trafiło do save()
        verify(agreementRepository).save(agreementCaptor.capture());
        Agreement savedAgreement = agreementCaptor.getValue();

        // 1. Sprawdzamy czy przypisano firmę
        assertThat(savedAgreement.getCompany()).isEqualTo(companyEntity);

        // 2. Sprawdzamy kluczową logikę biznesową: czy data została obliczona poprawnie
        // signedDate (2023-01-15) + 12 miesięcy = 2024-01-15
        LocalDate expectedDate = signedDate.plusMonths(periodMonths);
        assertThat(savedAgreement.getNextServiceDate()).isEqualTo(expectedDate);
    }



    @Test
    @DisplayName("findAll: Powinien zwrócić listę DTO")
    void findAll_ShouldReturnListOfDto() {
        // Given
        Agreement agreement = new Agreement();
        AgreementDto dto = new AgreementDto();

        when(agreementRepository.findAll()).thenReturn(List.of(agreement));
        mapperMock.when(() -> Mapper.toDto(agreement)).thenReturn(dto);

        // When
        List<AgreementDto> result = agreementService.findAll();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(dto);
    }

    @Test
    @DisplayName("search: Powinien filtrować używając specyfikacji")
    void search_ShouldFilterUsingSpecification() {
        // Given
        AgreementSC criteria = new AgreementSC();
        Agreement agreement = new Agreement();
        AgreementDto dto = new AgreementDto();

        // Ponieważ builder tworzony jest wewnątrz metody, mockujemy wywołanie repozytorium z dowolną specyfikacją
        when(agreementRepository.findAll(any(Specification.class))).thenReturn(List.of(agreement));
        mapperMock.when(() -> Mapper.toDto(agreement)).thenReturn(dto);

        // When
        List<AgreementDto> result = agreementService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).contains(dto);
        verify(agreementRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("findByNextRun: Powinien zwrócić listę encji na podstawie daty")
    void findByNextRun_ShouldReturnEntities() {
        // Given
        LocalDate nextRunDate = LocalDate.now().plusDays(5);
        List<Agreement> agreements = List.of(new Agreement());

        when(agreementRepository.findAllByNextServiceDate(nextRunDate)).thenReturn(agreements);

        // When
        List<Agreement> result = agreementService.findByNextRun(nextRunDate);

        // Then
        assertThat(result).isSameAs(agreements);
        verify(agreementRepository).findAllByNextServiceDate(nextRunDate);
        // Mapper nie powinien być wołany
        mapperMock.verifyNoInteractions();
    }

    @Test
    @DisplayName("findByNextRun: Powinien zwrócić pustą listę, gdy brak wyników")
    void findByNextRun_ShouldReturnEmptyList() {
        // Given
        LocalDate date = LocalDate.now();
        when(agreementRepository.findAllByNextServiceDate(date)).thenReturn(Collections.emptyList());

        // When
        List<Agreement> result = agreementService.findByNextRun(date);

        // Then
        assertThat(result).isEmpty();
    }
}
