package supportly.supportlybackend;


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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import supportly.supportlybackend.Criteria.EmployeeSC;
import supportly.supportlybackend.Dto.EmployeeDto;
import supportly.supportlybackend.Mapper.Mapper;
import supportly.supportlybackend.Model.Employee;
import supportly.supportlybackend.Repository.EmployeeRepository;
import supportly.supportlybackend.Service.EmployeeService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

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
    @DisplayName("findEmployeeByIndividualId: Powinien zwrócić pracownika, gdy istnieje")
    void findEmployeeByIndividualId_ShouldReturnEmployee() {
        // Given
        Long id = 1L;
        Employee employee = new Employee();
        employee.setIndividualId(id);

        when(employeeRepository.findByIndividualId(id)).thenReturn(Optional.of(employee));

        // When
        Employee result = employeeService.findEmployeeByIndividualId(id);

        // Then
        assertThat(result).isEqualTo(employee);
    }

    @Test
    @DisplayName("findEmployeeByIndividualId: Powinien rzucić wyjątek, gdy brak pracownika")
    void findEmployeeByIndividualId_ShouldThrowException_WhenNotFound() {
        // Given
        Long id = 99L;
        when(employeeRepository.findByIndividualId(id)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> employeeService.findEmployeeByIndividualId(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Nie znaleziono pracownika o id: " + id);
    }

    @Test
    @DisplayName("findAllEmployees: Powinien zwrócić listę wszystkich pracowników")
    void findAllEmployees_ShouldReturnList() {
        // Given
        List<Employee> employees = List.of(new Employee(), new Employee());
        when(employeeRepository.findAll()).thenReturn(employees);

        // When
        List<Employee> result = employeeService.findAllEmployees();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).isSameAs(employees);
    }

    @Test
    @DisplayName("add: Powinien zmapować DTO i zapisać encję")
    void add_ShouldMapAndSave() {
        // Given
        EmployeeDto dto = new EmployeeDto();
        Employee entity = new Employee();
        Employee savedEntity = new Employee();
        savedEntity.setIndividualId(100L);

        mapperMock.when(() -> Mapper.toEntity(dto)).thenReturn(entity);
        when(employeeRepository.save(entity)).thenReturn(savedEntity);

        // When
        Employee result = employeeService.add(dto);

        // Then
        assertThat(result).isEqualTo(savedEntity);
        verify(employeeRepository).save(entity);
        mapperMock.verify(() -> Mapper.toEntity(dto));
    }

    @Test
    @DisplayName("search: Powinien filtrować i mapować wyniki do DTO")
    void search_ShouldFilterAndMapToDto() {
        // Given
        EmployeeSC criteria = new EmployeeSC();
        Employee employee = new Employee();
        EmployeeDto dto = new EmployeeDto();

        // Mock repo: używamy any(Specification.class) bo builder jest wewnątrz metody
        when(employeeRepository.findAll(any(Specification.class))).thenReturn(List.of(employee));
        mapperMock.when(() -> Mapper.toDto(employee)).thenReturn(dto);

        // When
        List<EmployeeDto> result = employeeService.search(criteria);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(dto);
        verify(employeeRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("findEmployeeByNumberPhone: Powinien zwrócić Optional z pracownikiem")
    void findEmployeeByNumberPhone_ShouldReturnOptional() {
        // Given
        String phone = "123-456-789";
        Employee employee = new Employee();
        when(employeeRepository.findByPhoneNumber(phone)).thenReturn(Optional.of(employee));

        // When
        Optional<Employee> result = employeeService.findEmployeeByNumberPhone(phone);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(employee);
    }

    @Test
    @DisplayName("updateEmployee: Powinien zaktualizować dane istniejącego pracownika")
    void updateEmployee_ShouldUpdateFieldsAndSave_WhenExists() {
        // Given
        Long id = 50L;

        // Dane wejściowe (nowe wartości)
        Employee inputEmployee = new Employee();
        inputEmployee.setIndividualId(id);
        inputEmployee.setFirstName("Jan");
        inputEmployee.setLastName("Kowalski");
        inputEmployee.setSecondName("Piotr");
        inputEmployee.setPhoneNumber("999888777");

        // Istniejący pracownik w bazie (stare wartości)
        Employee existingEmployee = new Employee();
        existingEmployee.setIndividualId(id);
        existingEmployee.setFirstName("StareImie");
        existingEmployee.setLastName("StareNazwisko");

        when(employeeRepository.findByIndividualId(id)).thenReturn(Optional.of(existingEmployee));
        // Mock save zwraca obiekt, który został mu przekazany (zaktualizowany)
        when(employeeRepository.save(existingEmployee)).thenReturn(existingEmployee);

        // When
        Employee result = employeeService.updateEmployee(inputEmployee);

        // Then
        // Weryfikujemy czy pola w existingEmployee zostały nadpisane wartościami z inputEmployee
        assertThat(existingEmployee.getFirstName()).isEqualTo("Jan");
        assertThat(existingEmployee.getLastName()).isEqualTo("Kowalski");
        assertThat(existingEmployee.getSecondName()).isEqualTo("Piotr");
        assertThat(existingEmployee.getPhoneNumber()).isEqualTo("999888777");

        verify(employeeRepository).save(existingEmployee);
    }

    @Test
    @DisplayName("updateEmployee: Powinien rzucić wyjątek, gdy pracownik nie istnieje")
    void updateEmployee_ShouldThrowException_WhenNotFound() {
        // Given
        Long id = 50L;
        Employee inputEmployee = new Employee();
        inputEmployee.setIndividualId(id);

        when(employeeRepository.findByIndividualId(id)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> employeeService.updateEmployee(inputEmployee))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Nie zaleziono takiego pracownika"); // Tekst zgodny z kodem serwisu (literówka "zaleziono")

        verify(employeeRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteEmployeeById: Powinien wywołać deleteById w repozytorium")
    void deleteEmployeeById_ShouldCallDelete() {
        // Given
        Long id = 123L;

        // When
        employeeService.deleteEmployeeById(id);

        // Then
        verify(employeeRepository).deleteById(id);
    }

    @Test
    @DisplayName("findEmployeeByLastName: Powinien zwrócić listę znalezioną przez repozytorium")
    void findEmployeeByLastName_ShouldReturnList() {
        // Given
        String lastName = "Nowak";
        List<Employee> employees = List.of(new Employee());

        when(employeeRepository.findAllByLastNameContainingIgnoreCase(lastName)).thenReturn(employees);

        // When
        List<Employee> result = employeeService.findEmployeeByLastName(lastName);

        // Then
        assertThat(result).isSameAs(employees);
    }
}