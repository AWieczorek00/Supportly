package supportly.supportlybackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import supportly.supportlybackend.Criteria.EmployeeSC;
import supportly.supportlybackend.Criteria.GenericSpecificationBuilder;
import supportly.supportlybackend.Dto.EmployeeDto;
import supportly.supportlybackend.Mapper.Mapper;
import supportly.supportlybackend.Model.Employee;
import supportly.supportlybackend.Repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee findEmployeeByIndividualId(Long individualId) {
        return employeeRepository.findByIndividualId(individualId)
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono pracownika o ids: " + individualId));
    }

    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    @Transactional
    public Employee add(EmployeeDto employeeBody) {
        Employee employee = Mapper.toEntity(employeeBody);
        return null;
    }

    public List<EmployeeDto> search(EmployeeSC criteria) {
        GenericSpecificationBuilder<Employee> builder = new GenericSpecificationBuilder<>();
        Specification<Employee> spec = builder.build(criteria);
        return employeeRepository.findAll(spec).stream().map(Mapper::toDto).collect(Collectors.toList());
    }

    public Optional<Employee> findEmployeeByNumberPhone(String numberPhone) {
        return employeeRepository.findByPhoneNumber(numberPhone);
    }

    @Transactional
    public Employee updateEmployee(Employee employeeBody) {
        return employeeRepository.findByIndividualId(employeeBody.getIndividualId())
                .map(employeeUpdate -> {
                    employeeUpdate.setFirstName(employeeBody.getFirstName());
                    employeeUpdate.setSecondName(employeeBody.getSecondName());
                    employeeUpdate.setLastName(employeeBody.getLastName());
                    employeeUpdate.setPhoneNumber(employeeBody.getPhoneNumber());
                    return employeeRepository.save(employeeUpdate);
                }).orElseThrow(() -> new ResourceNotFoundException("Nie zaleziono takiego pracownika"));
    }

    public void deleteEmployeeById(Long individualId) {
        employeeRepository.deleteById(individualId);
    }

    public List<Employee> findEmployeeByLastName(String lastName) {
        return employeeRepository.findAllByLastNameContainingIgnoreCase(lastName);
    }
}
