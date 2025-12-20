package supportly.supportlybackend;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import supportly.supportlybackend.Criteria.EmployeeSC;
import supportly.supportlybackend.Dto.EmployeeDto;
import supportly.supportlybackend.Mapper.Mapper;
import supportly.supportlybackend.Repository.EmployeeRepository;
import supportly.supportlybackend.Service.EmployeeService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
        List<EmployeeDto> employees = List.of(new EmployeeDto("Alice", " ", "Nowak", "111111111"), new EmployeeDto("Bob", " ", "Kowalski", "222222222"));
        employeeRepository.saveAll(employees.stream().map(Mapper::toEntity).toList());
    }

    @Test
    void shouldAddCompany() {
        // given
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setFirstName("John");
        employeeDto.setLastName("Doe");
        employeeDto.setPhoneNumber("987654321");


        // when
        employeeService.add(employeeDto);

        // then
        assertThat(employeeRepository.findAll()).hasSize(3);
        assertThat(employeeRepository.findAll().get(2).getFirstName()).isEqualTo("John");
    }

    @Test
    void searchEmployeeByCriteria() {
        // given
        EmployeeSC criteria = new EmployeeSC();
        criteria.setFirstName("Ali");
        EmployeeSC employeeCriteria = new EmployeeSC();
        employeeCriteria.setLastName("Kowal");


        // when
        List<EmployeeDto> result = employeeService.search(criteria);
        List<EmployeeDto> result2 = employeeService.search(employeeCriteria);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getFirstName()).isEqualTo("Alice");
        assertThat(result2).hasSize(1);
        assertThat(result2.getFirst().getLastName()).isEqualTo("Kowalski");

    }


    @Test
    void searchEmpty() {
        // given
        EmployeeSC criteria = new EmployeeSC();
        criteria.setPhoneNumber("1111444");

        // when
        List<EmployeeDto> result = employeeService.search(criteria);

        // then
        assertThat(result).isEmpty();
    }

}
