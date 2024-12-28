package supportly.supportlybackend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import supportly.supportlybackend.Model.Employee;
import supportly.supportlybackend.Service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/employee")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @GetMapping("/all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Employee>> getAllEmployee() {
        List<Employee> employeeList = employeeService.findAllEmployees();
        return new ResponseEntity<>(employeeList, HttpStatus.OK);
    }

    @GetMapping("/{individualId}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long individualId) {
        Employee employee = employeeService.findEmployeeByIndividualId(individualId);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employeeBody) {
        Employee employee = employeeService.createNewEmployee(employeeBody);
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employeeBody) {
        Employee employee = employeeService.updateEmployee(employeeBody);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{individualId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long individualId) {
        employeeService.deleteEmployeeById(individualId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
