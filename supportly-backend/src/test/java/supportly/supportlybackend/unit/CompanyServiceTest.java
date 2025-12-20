package supportly.supportlybackend.unit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import supportly.supportlybackend.Dto.CompanyDto;
import supportly.supportlybackend.Mapper.Mapper;
import supportly.supportlybackend.Model.Company;
import supportly.supportlybackend.Repository.CompanyRepository;
import supportly.supportlybackend.Service.CompanyService;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyService companyService;

    // Obiekt do mockowania metod statycznych klasy Mapper
    private MockedStatic<Mapper> mapperMock;

    @BeforeEach
    void setUp() {
        mapperMock = Mockito.mockStatic(Mapper.class);
    }

    @AfterEach
    void tearDown() {
        mapperMock.close();
    }

    @Test
    @DisplayName("add: Powinien wywołać save w repozytorium")
    void add_ShouldCallRepositorySave() {
        // Given
        Company company = new Company();
        company.setName("Test Company");

        // When
        companyService.add(company);

        // Then
        verify(companyRepository, times(1)).save(company);
    }

    @Test
    @DisplayName("findAllByName: Powinien zwrócić listę zmapowanych DTO")
    void findAllByName_ShouldReturnMappedDtos() {
        // Given
        String searchName = "Soft";
        Company companyEntity = new Company();
        companyEntity.setName("Software House");

        CompanyDto companyDto = new CompanyDto();
        companyDto.setName("Software House");

        // Mockowanie repozytorium
        when(companyRepository.findAllByNameContains(searchName))
                .thenReturn(List.of(companyEntity));

        // Mockowanie mappera
        mapperMock.when(() -> Mapper.toDto(companyEntity)).thenReturn(companyDto);

        // When
        List<CompanyDto> result = companyService.findAllByName(searchName);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(companyDto);

        verify(companyRepository).findAllByNameContains(searchName);
        mapperMock.verify(() -> Mapper.toDto(companyEntity));
    }

    @Test
    @DisplayName("findAllByName: Powinien zwrócić pustą listę, gdy repozytorium nic nie znajdzie")
    void findAllByName_ShouldReturnEmptyList_WhenNotFound() {
        // Given
        String searchName = "NonExistent";

        when(companyRepository.findAllByNameContains(searchName))
                .thenReturn(Collections.emptyList());

        // When
        List<CompanyDto> result = companyService.findAllByName(searchName);

        // Then
        assertThat(result).isEmpty();
        // Upewniamy się, że mapper nie był wołany, bo strumień był pusty
        mapperMock.verifyNoInteractions();
    }

    @Test
    @DisplayName("findByNip: Powinien zwrócić firmę znalezioną przez repozytorium")
    void findByNip_ShouldReturnCompany() {
        // Given
        String nip = "1234567890";
        Company company = new Company();
        company.setNip(nip);

        when(companyRepository.findByNip(nip)).thenReturn(company);

        // When
        Company result = companyService.findByNip(nip);

        // Then
        assertThat(result).isEqualTo(company);
        verify(companyRepository).findByNip(nip);
    }

    @Test
    @DisplayName("findByNip: Powinien zwrócić null, jeśli repozytorium zwróci null")
    void findByNip_ShouldReturnNull_WhenNotFound() {
        // Given
        String nip = "0000000000";

        // Domyślne zachowanie mocka to zwracanie null dla obiektów,
        // ale dla czytelności można to zapisać jawnie:
        when(companyRepository.findByNip(nip)).thenReturn(null);

        // When
        Company result = companyService.findByNip(nip);

        // Then
        assertThat(result).isNull();
    }
}