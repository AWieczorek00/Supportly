package supportly.supportlybackend;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.test.context.TestPropertySource;
import supportly.supportlybackend.Criteria.EmployeeSC;
import supportly.supportlybackend.Dto.EmployeeDto;
import supportly.supportlybackend.Mapper.Mapper;
import supportly.supportlybackend.Model.Employee;
import supportly.supportlybackend.Repository.EmployeeRepository;
import supportly.supportlybackend.Service.EmployeeService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:/application-test.properties")
@RequiredArgsConstructor
public class EmployeeServiceIT {


    @Autowired
    private EmployeeRepository employeeRepository;


    @Autowired
    private EmployeeService employeeService;


@BeforeEach
void setUp() {
    // Czyścimy tabelę przed każdym testem
    employeeRepository.deleteAll();
}

    @Test
    @DisplayName("add: Powinien zapisać nowego pracownika w bazie")
    void shouldSaveNewEmployee() {
        // Given
        EmployeeDto dto = new EmployeeDto();
        dto.setFirstName("Jan");
        dto.setLastName("Kowalski");
        dto.setPhoneNumber("123456789");
        dto.setSecondName("Piotr");

        // When
        Employee savedEmployee = employeeService.add(dto);

        // Then
        assertThat(savedEmployee.getIndividualId()).isNotNull(); // Sprawdzamy czy ID zostało wygenerowane

        // Weryfikacja w bazie
        Optional<Employee> inDb = employeeRepository.findByIndividualId(savedEmployee.getIndividualId());
        assertThat(inDb).isPresent();
        assertThat(inDb.get().getFirstName()).isEqualTo("Jan");
        assertThat(inDb.get().getLastName()).isEqualTo("Kowalski");
    }

    @Test
    @DisplayName("findEmployeeByIndividualId: Powinien znaleźć istniejącego pracownika")
    void shouldFindEmployeeById() {
        // Given
        Employee emp = createAndSaveEmployee("Anna", "Nowak", "987654321");

        // When
        Employee result = employeeService.findEmployeeByIndividualId(emp.getIndividualId());

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getLastName()).isEqualTo("Nowak");
    }

    @Test
    @DisplayName("findEmployeeByIndividualId: Powinien rzucić wyjątek, gdy ID nie istnieje")
    void shouldThrowExceptionWhenIdNotFound() {
        // Given
        Long nonExistentId = 9999L;

        // When & Then
        assertThatThrownBy(() -> employeeService.findEmployeeByIndividualId(nonExistentId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Nie znaleziono pracownika o id: " + nonExistentId);
    }

    @Test
    @DisplayName("updateEmployee: Powinien zaktualizować dane pracownika")
    void shouldUpdateEmployeeData2() {
        // Given
        Employee original = createAndSaveEmployee("Tomasz", "Stary", "111222333");

        // Obiekt z nowymi danymi (musi mieć to samo ID co oryginał)
        Employee updateData = new Employee();
        updateData.setIndividualId(original.getIndividualId());
        updateData.setFirstName("Tomasz");
        updateData.setLastName("Nowy"); // Zmiana nazwiska
        updateData.setSecondName("Adam");
        updateData.setPhoneNumber("999888777"); // Zmiana telefonu

        // When
        Employee updated = employeeService.updateEmployee(updateData);

        // Then
        // Sprawdź czy zwrócony obiekt jest zaktualizowany
        assertThat(updated.getLastName()).isEqualTo("Nowy");
        assertThat(updated.getPhoneNumber()).isEqualTo("999888777");

        // Sprawdź czy w bazie faktycznie się zmieniło
        Employee fromDb = employeeRepository.findById(original.getIndividualId()).orElseThrow();
        assertThat(fromDb.getLastName()).isEqualTo("Nowy");
    }

    @Test
    @DisplayName("updateEmployee: Powinien zaktualizować dane pracownika")
    void shouldUpdateEmployeeData3() {
        // Given
        Employee original = createAndSaveEmployee("Tomasz", "Stary", "111222333");

        // Obiekt z nowymi danymi (musi mieć to samo ID co oryginał)
        Employee updateData = new Employee();
        updateData.setIndividualId(original.getIndividualId());
        updateData.setFirstName("Tomasz");
        updateData.setLastName("Nowy"); // Zmiana nazwiska
        updateData.setSecondName("Adam");
        updateData.setPhoneNumber("999888777"); // Zmiana telefonu

        // When
        Employee updated = employeeService.updateEmployee(updateData);

        // Then
        // Sprawdź czy zwrócony obiekt jest zaktualizowany
        assertThat(updated.getLastName()).isEqualTo("Nowy");
        assertThat(updated.getPhoneNumber()).isEqualTo("999888777");

        // Sprawdź czy w bazie faktycznie się zmieniło
        Employee fromDb = employeeRepository.findById(original.getIndividualId()).orElseThrow();
        assertThat(fromDb.getLastName()).isEqualTo("Nowy");
    }


    @Test
    @DisplayName("updateEmployee: Powinien zaktualizować dane pracownika")
    void shouldUpdateEmployeeData4() {
        // Given
        Employee original = createAndSaveEmployee("Tomasz", "Stary", "111222333");

        // Obiekt z nowymi danymi (musi mieć to samo ID co oryginał)
        Employee updateData = new Employee();
        updateData.setIndividualId(original.getIndividualId());
        updateData.setFirstName("Tomasz");
        updateData.setLastName("Nowy"); // Zmiana nazwiska
        updateData.setSecondName("Adam");
        updateData.setPhoneNumber("999888777"); // Zmiana telefonu

        // When
        Employee updated = employeeService.updateEmployee(updateData);

        // Then
        // Sprawdź czy zwrócony obiekt jest zaktualizowany
        assertThat(updated.getLastName()).isEqualTo("Nowy");
        assertThat(updated.getPhoneNumber()).isEqualTo("999888777");

        // Sprawdź czy w bazie faktycznie się zmieniło
        Employee fromDb = employeeRepository.findById(original.getIndividualId()).orElseThrow();
        assertThat(fromDb.getLastName()).isEqualTo("Nowy");
    }

    @Test
    @DisplayName("updateEmployee: Powinien zaktualizować dane pracownika")
    void shouldUpdateEmployeeData5() {
        // Given
        Employee original = createAndSaveEmployee("Tomasz", "Stary", "111222333");

        // Obiekt z nowymi danymi (musi mieć to samo ID co oryginał)
        Employee updateData = new Employee();
        updateData.setIndividualId(original.getIndividualId());
        updateData.setFirstName("Tomasz");
        updateData.setLastName("Nowy"); // Zmiana nazwiska
        updateData.setSecondName("Adam");
        updateData.setPhoneNumber("999888777"); // Zmiana telefonu

        // When
        Employee updated = employeeService.updateEmployee(updateData);

        // Then
        // Sprawdź czy zwrócony obiekt jest zaktualizowany
        assertThat(updated.getLastName()).isEqualTo("Nowy");
        assertThat(updated.getPhoneNumber()).isEqualTo("999888777");

        // Sprawdź czy w bazie faktycznie się zmieniło
        Employee fromDb = employeeRepository.findById(original.getIndividualId()).orElseThrow();
        assertThat(fromDb.getLastName()).isEqualTo("Nowy");
    }

    @Test
    @DisplayName("updateEmployee: Powinien zaktualizować dane pracownika")
    void shouldUpdateEmployeeData6() {
        // Given
        Employee original = createAndSaveEmployee("Tomasz", "Stary", "111222333");

        // Obiekt z nowymi danymi (musi mieć to samo ID co oryginał)
        Employee updateData = new Employee();
        updateData.setIndividualId(original.getIndividualId());
        updateData.setFirstName("Tomasz");
        updateData.setLastName("Nowy"); // Zmiana nazwiska
        updateData.setSecondName("Adam");
        updateData.setPhoneNumber("999888777"); // Zmiana telefonu

        // When
        Employee updated = employeeService.updateEmployee(updateData);

        // Then
        // Sprawdź czy zwrócony obiekt jest zaktualizowany
        assertThat(updated.getLastName()).isEqualTo("Nowy");
        assertThat(updated.getPhoneNumber()).isEqualTo("999888777");

        // Sprawdź czy w bazie faktycznie się zmieniło
        Employee fromDb = employeeRepository.findById(original.getIndividualId()).orElseThrow();
        assertThat(fromDb.getLastName()).isEqualTo("Nowy");
    }

    @Test
    @DisplayName("updateEmployee: Powinien zaktualizować dane pracownika")
    void shouldUpdateEmployeeData7() {
        // Given
        Employee original = createAndSaveEmployee("Tomasz", "Stary", "111222333");

        // Obiekt z nowymi danymi (musi mieć to samo ID co oryginał)
        Employee updateData = new Employee();
        updateData.setIndividualId(original.getIndividualId());
        updateData.setFirstName("Tomasz");
        updateData.setLastName("Nowy"); // Zmiana nazwiska
        updateData.setSecondName("Adam");
        updateData.setPhoneNumber("999888777"); // Zmiana telefonu

        // When
        Employee updated = employeeService.updateEmployee(updateData);

        // Then
        // Sprawdź czy zwrócony obiekt jest zaktualizowany
        assertThat(updated.getLastName()).isEqualTo("Nowy");
        assertThat(updated.getPhoneNumber()).isEqualTo("999888777");

        // Sprawdź czy w bazie faktycznie się zmieniło
        Employee fromDb = employeeRepository.findById(original.getIndividualId()).orElseThrow();
        assertThat(fromDb.getLastName()).isEqualTo("Nowy");
    }

    @Test
    @DisplayName("updateEmployee: Powinien zaktualizować dane pracownika")
    void shouldUpdateEmployeeData8() {
        // Given
        Employee original = createAndSaveEmployee("Tomasz", "Stary", "111222333");

        // Obiekt z nowymi danymi (musi mieć to samo ID co oryginał)
        Employee updateData = new Employee();
        updateData.setIndividualId(original.getIndividualId());
        updateData.setFirstName("Tomasz");
        updateData.setLastName("Nowy"); // Zmiana nazwiska
        updateData.setSecondName("Adam");
        updateData.setPhoneNumber("999888777"); // Zmiana telefonu

        // When
        Employee updated = employeeService.updateEmployee(updateData);

        // Then
        // Sprawdź czy zwrócony obiekt jest zaktualizowany
        assertThat(updated.getLastName()).isEqualTo("Nowy");
        assertThat(updated.getPhoneNumber()).isEqualTo("999888777");

        // Sprawdź czy w bazie faktycznie się zmieniło
        Employee fromDb = employeeRepository.findById(original.getIndividualId()).orElseThrow();
        assertThat(fromDb.getLastName()).isEqualTo("Nowy");
    }

    @Test
    @DisplayName("updateEmployee: Powinien zaktualizować dane pracownika")
    void shouldUpdateEmployeeData9() {
        // Given
        Employee original = createAndSaveEmployee("Tomasz", "Stary", "111222333");

        // Obiekt z nowymi danymi (musi mieć to samo ID co oryginał)
        Employee updateData = new Employee();
        updateData.setIndividualId(original.getIndividualId());
        updateData.setFirstName("Tomasz");
        updateData.setLastName("Nowy"); // Zmiana nazwiska
        updateData.setSecondName("Adam");
        updateData.setPhoneNumber("999888777"); // Zmiana telefonu

        // When
        Employee updated = employeeService.updateEmployee(updateData);

        // Then
        // Sprawdź czy zwrócony obiekt jest zaktualizowany
        assertThat(updated.getLastName()).isEqualTo("Nowy");
        assertThat(updated.getPhoneNumber()).isEqualTo("999888777");

        // Sprawdź czy w bazie faktycznie się zmieniło
        Employee fromDb = employeeRepository.findById(original.getIndividualId()).orElseThrow();
        assertThat(fromDb.getLastName()).isEqualTo("Nowy");
    }

    @Test
    @DisplayName("updateEmployee: Powinien zaktualizować dane pracownika")
    void shouldUpdateEmployeeData10() {
        // Given
        Employee original = createAndSaveEmployee("Tomasz", "Stary", "111222333");

        // Obiekt z nowymi danymi (musi mieć to samo ID co oryginał)
        Employee updateData = new Employee();
        updateData.setIndividualId(original.getIndividualId());
        updateData.setFirstName("Tomasz");
        updateData.setLastName("Nowy"); // Zmiana nazwiska
        updateData.setSecondName("Adam");
        updateData.setPhoneNumber("999888777"); // Zmiana telefonu

        // When
        Employee updated = employeeService.updateEmployee(updateData);

        // Then
        // Sprawdź czy zwrócony obiekt jest zaktualizowany
        assertThat(updated.getLastName()).isEqualTo("Nowy");
        assertThat(updated.getPhoneNumber()).isEqualTo("999888777");

        // Sprawdź czy w bazie faktycznie się zmieniło
        Employee fromDb = employeeRepository.findById(original.getIndividualId()).orElseThrow();
        assertThat(fromDb.getLastName()).isEqualTo("Nowy");
    }

    @Test
    @DisplayName("updateEmployee: Powinien zaktualizować dane pracownika")
    void shouldUpdateEmployeeData11() {
        // Given
        Employee original = createAndSaveEmployee("Tomasz", "Stary", "111222333");

        // Obiekt z nowymi danymi (musi mieć to samo ID co oryginał)
        Employee updateData = new Employee();
        updateData.setIndividualId(original.getIndividualId());
        updateData.setFirstName("Tomasz");
        updateData.setLastName("Nowy"); // Zmiana nazwiska
        updateData.setSecondName("Adam");
        updateData.setPhoneNumber("999888777"); // Zmiana telefonu

        // When
        Employee updated = employeeService.updateEmployee(updateData);

        // Then
        // Sprawdź czy zwrócony obiekt jest zaktualizowany
        assertThat(updated.getLastName()).isEqualTo("Nowy");
        assertThat(updated.getPhoneNumber()).isEqualTo("999888777");

        // Sprawdź czy w bazie faktycznie się zmieniło
        Employee fromDb = employeeRepository.findById(original.getIndividualId()).orElseThrow();
        assertThat(fromDb.getLastName()).isEqualTo("Nowy");
    }

    @Test
    @DisplayName("updateEmployee: Powinien zaktualizować dane pracownika")
    void shouldUpdateEmployeeData12() {
        // Given
        Employee original = createAndSaveEmployee("Tomasz", "Stary", "111222333");

        // Obiekt z nowymi danymi (musi mieć to samo ID co oryginał)
        Employee updateData = new Employee();
        updateData.setIndividualId(original.getIndividualId());
        updateData.setFirstName("Tomasz");
        updateData.setLastName("Nowy"); // Zmiana nazwiska
        updateData.setSecondName("Adam");
        updateData.setPhoneNumber("999888777"); // Zmiana telefonu

        // When
        Employee updated = employeeService.updateEmployee(updateData);

        // Then
        // Sprawdź czy zwrócony obiekt jest zaktualizowany
        assertThat(updated.getLastName()).isEqualTo("Nowy");
        assertThat(updated.getPhoneNumber()).isEqualTo("999888777");

        // Sprawdź czy w bazie faktycznie się zmieniło
        Employee fromDb = employeeRepository.findById(original.getIndividualId()).orElseThrow();
        assertThat(fromDb.getLastName()).isEqualTo("Nowy");
    }

    @Test
    @DisplayName("updateEmployee: Powinien zaktualizować dane pracownika")
    void shouldUpdateEmployeeData13() {
        // Given
        Employee original = createAndSaveEmployee("Tomasz", "Stary", "111222333");

        // Obiekt z nowymi danymi (musi mieć to samo ID co oryginał)
        Employee updateData = new Employee();
        updateData.setIndividualId(original.getIndividualId());
        updateData.setFirstName("Tomasz");
        updateData.setLastName("Nowy"); // Zmiana nazwiska
        updateData.setSecondName("Adam");
        updateData.setPhoneNumber("999888777"); // Zmiana telefonu

        // When
        Employee updated = employeeService.updateEmployee(updateData);

        // Then
        // Sprawdź czy zwrócony obiekt jest zaktualizowany
        assertThat(updated.getLastName()).isEqualTo("Nowy");
        assertThat(updated.getPhoneNumber()).isEqualTo("999888777");

        // Sprawdź czy w bazie faktycznie się zmieniło
        Employee fromDb = employeeRepository.findById(original.getIndividualId()).orElseThrow();
        assertThat(fromDb.getLastName()).isEqualTo("Nowy");
    }

    @Test
    @DisplayName("updateEmployee: Powinien zaktualizować dane pracownika")
    void shouldUpdateEmployeeData14() {
        // Given
        Employee original = createAndSaveEmployee("Tomasz", "Stary", "111222333");

        // Obiekt z nowymi danymi (musi mieć to samo ID co oryginał)
        Employee updateData = new Employee();
        updateData.setIndividualId(original.getIndividualId());
        updateData.setFirstName("Tomasz");
        updateData.setLastName("Nowy"); // Zmiana nazwiska
        updateData.setSecondName("Adam");
        updateData.setPhoneNumber("999888777"); // Zmiana telefonu

        // When
        Employee updated = employeeService.updateEmployee(updateData);

        // Then
        // Sprawdź czy zwrócony obiekt jest zaktualizowany
        assertThat(updated.getLastName()).isEqualTo("Nowy");
        assertThat(updated.getPhoneNumber()).isEqualTo("999888777");

        // Sprawdź czy w bazie faktycznie się zmieniło
        Employee fromDb = employeeRepository.findById(original.getIndividualId()).orElseThrow();
        assertThat(fromDb.getLastName()).isEqualTo("Nowy");
    }

    @Test
    @DisplayName("updateEmployee: Powinien zaktualizować dane pracownika")
    void shouldUpdateEmployeeData15() {
        // Given
        Employee original = createAndSaveEmployee("Tomasz", "Stary", "111222333");

        // Obiekt z nowymi danymi (musi mieć to samo ID co oryginał)
        Employee updateData = new Employee();
        updateData.setIndividualId(original.getIndividualId());
        updateData.setFirstName("Tomasz");
        updateData.setLastName("Nowy"); // Zmiana nazwiska
        updateData.setSecondName("Adam");
        updateData.setPhoneNumber("999888777"); // Zmiana telefonu

        // When
        Employee updated = employeeService.updateEmployee(updateData);

        // Then
        // Sprawdź czy zwrócony obiekt jest zaktualizowany
        assertThat(updated.getLastName()).isEqualTo("Nowy");
        assertThat(updated.getPhoneNumber()).isEqualTo("999888777");

        // Sprawdź czy w bazie faktycznie się zmieniło
        Employee fromDb = employeeRepository.findById(original.getIndividualId()).orElseThrow();
        assertThat(fromDb.getLastName()).isEqualTo("Nowy");
    }

    @Test
    @DisplayName("updateEmployee: Powinien zaktualizować dane pracownika")
    void shouldUpdateEmployeeData16() {
        // Given
        Employee original = createAndSaveEmployee("Tomasz", "Stary", "111222333");

        // Obiekt z nowymi danymi (musi mieć to samo ID co oryginał)
        Employee updateData = new Employee();
        updateData.setIndividualId(original.getIndividualId());
        updateData.setFirstName("Tomasz");
        updateData.setLastName("Nowy"); // Zmiana nazwiska
        updateData.setSecondName("Adam");
        updateData.setPhoneNumber("999888777"); // Zmiana telefonu

        // When
        Employee updated = employeeService.updateEmployee(updateData);

        // Then
        // Sprawdź czy zwrócony obiekt jest zaktualizowany
        assertThat(updated.getLastName()).isEqualTo("Nowy");
        assertThat(updated.getPhoneNumber()).isEqualTo("999888777");

        // Sprawdź czy w bazie faktycznie się zmieniło
        Employee fromDb = employeeRepository.findById(original.getIndividualId()).orElseThrow();
        assertThat(fromDb.getLastName()).isEqualTo("Nowy");
    }

    @Test
    @DisplayName("updateEmployee: Powinien zaktualizować dane pracownika")
    void shouldUpdateEmployeeData17() {
        // Given
        Employee original = createAndSaveEmployee("Tomasz", "Stary", "111222333");

        // Obiekt z nowymi danymi (musi mieć to samo ID co oryginał)
        Employee updateData = new Employee();
        updateData.setIndividualId(original.getIndividualId());
        updateData.setFirstName("Tomasz");
        updateData.setLastName("Nowy"); // Zmiana nazwiska
        updateData.setSecondName("Adam");
        updateData.setPhoneNumber("999888777"); // Zmiana telefonu

        // When
        Employee updated = employeeService.updateEmployee(updateData);

        // Then
        // Sprawdź czy zwrócony obiekt jest zaktualizowany
        assertThat(updated.getLastName()).isEqualTo("Nowy");
        assertThat(updated.getPhoneNumber()).isEqualTo("999888777");

        // Sprawdź czy w bazie faktycznie się zmieniło
        Employee fromDb = employeeRepository.findById(original.getIndividualId()).orElseThrow();
        assertThat(fromDb.getLastName()).isEqualTo("Nowy");
    }

    @Test
    @DisplayName("updateEmployee: Powinien zaktualizować dane pracownika")
    void shouldUpdateEmployeeData() {
        // Given
        Employee original = createAndSaveEmployee("Tomasz", "Stary", "111222333");

        // Obiekt z nowymi danymi (musi mieć to samo ID co oryginał)
        Employee updateData = new Employee();
        updateData.setIndividualId(original.getIndividualId());
        updateData.setFirstName("Tomasz");
        updateData.setLastName("Nowy"); // Zmiana nazwiska
        updateData.setSecondName("Adam");
        updateData.setPhoneNumber("999888777"); // Zmiana telefonu

        // When
        Employee updated = employeeService.updateEmployee(updateData);

        // Then
        // Sprawdź czy zwrócony obiekt jest zaktualizowany
        assertThat(updated.getLastName()).isEqualTo("Nowy");
        assertThat(updated.getPhoneNumber()).isEqualTo("999888777");

        // Sprawdź czy w bazie faktycznie się zmieniło
        Employee fromDb = employeeRepository.findById(original.getIndividualId()).orElseThrow();
        assertThat(fromDb.getLastName()).isEqualTo("Nowy");
    }


    @Test
    @DisplayName("updateEmployee: Powinien rzucić wyjątek przy próbie edycji nieistniejącego ID")
    void shouldThrowExceptionWhenUpdatingNonExistentEmployee() {
        // Given
        Employee ghostEmployee = new Employee();
        ghostEmployee.setIndividualId(9999L);
        ghostEmployee.setFirstName("Casper");

        // When & Then
        assertThatThrownBy(() -> employeeService.updateEmployee(ghostEmployee))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Nie zaleziono takiego pracownika");
    }

    @Test
    @DisplayName("deleteEmployeeById: Powinien usunąć pracownika z bazy")
    void shouldDeleteEmployee() {
        // Given
        Employee emp = createAndSaveEmployee("Marek", "DoKasacji", "555666777");
        Long id = emp.getIndividualId();

        // When
        employeeService.deleteEmployeeById(id);

        // Then
        Optional<Employee> deleted = employeeRepository.findByIndividualId(id);
        assertThat(deleted).isEmpty();
    }

    @Test
    @DisplayName("search: Powinien filtrować pracowników po nazwisku (Specification)")
    void shouldSearchEmployeesByCriteria() {
        // Given
        createAndSaveEmployee("Jan", "Kowalski", "111");
        createAndSaveEmployee("Adam", "Nowak", "222");
        createAndSaveEmployee("Ewa", "Kowalska", "333");

        // Kryteria: szukamy nazwiska "Kowalsk" (powinno znaleźć Kowalski i Kowalska jeśli builder używa 'like')
        // Lub dokładne dopasowanie - zależy od implementacji GenericSpecificationBuilder.
        // Zakładam tutaj proste dopasowanie po polu lastName.
        EmployeeSC criteria = new EmployeeSC();
        criteria.setLastName("Kowalski");

        // When
        List<EmployeeDto> results = employeeService.search(criteria);

        // Then
        // Spodziewamy się przynajmniej jednego wyniku
        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getLastName()).isEqualTo("Kowalski");
    }

    @Test
    @DisplayName("findEmployeeByNumberPhone: Powinien znaleźć po numerze telefonu")
    void shouldFindEmployeeByPhoneNumber() {
        // Given
        String phone = "500600700";
        createAndSaveEmployee("Test", "User", phone);

        // When
        Optional<Employee> result = employeeService.findEmployeeByNumberPhone(phone);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getPhoneNumber()).isEqualTo(phone);
    }

    // --- Metoda pomocnicza ---
    private Employee createAndSaveEmployee(String firstName, String lastName, String phone) {
        Employee emp = new Employee();
        emp.setFirstName(firstName);
        emp.setLastName(lastName);
        emp.setPhoneNumber(phone);
        // Ustawiamy inne wymagane pola, jeśli istnieją (np. email, secondName)
        emp.setSecondName("Middle");
        return employeeRepository.save(emp);
    }

}
