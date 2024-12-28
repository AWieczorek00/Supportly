package supportly.supportlybackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import supportly.supportlybackend.Model.Employee;
import supportly.supportlybackend.Repository.EmployeeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee findEmployeeByIndividualId(Long individualId) {
        return employeeRepository.findByIndividualId(individualId)
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono pracownika o id: " + individualId));
    }

    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee createNewEmployee(Employee employeeBody) {
        employeeBody.setIndividualId(generateIndividualIdForEmployee());
        employeeBody.setDateOfCreation(LocalDate.now());

        return employeeRepository.save(employeeBody);
    }

    @Transactional
    public Employee updateEmployee(Employee employeeBody) {
        return employeeRepository.findByIndividualId(employeeBody.getIndividualId())
                .map(employeeUpdate -> {
                    employeeUpdate.setFirstName(employeeBody.getFirstName());
                    employeeUpdate.setSecondName(employeeBody.getSecondName());
                    employeeUpdate.setLastName(employeeBody.getLastName());
                    employeeUpdate.setEmail(employeeBody.getEmail());
                    employeeUpdate.setPhoneNumber(employeeBody.getPhoneNumber());
                    return employeeRepository.save(employeeUpdate);
                }).orElseThrow(() -> new ResourceNotFoundException("Nie zaleziono takiego pracownika"));
    }

    public void deleteEmployeeById(Long individualId) {
        employeeRepository.deleteById(individualId);
    }

    private Long generateIndividualIdForEmployee() {
        Random random = new Random();
        return Long.valueOf(String.valueOf(LocalDate.now()).replace("-", "") + random.nextLong(10, 100));
    }
}
